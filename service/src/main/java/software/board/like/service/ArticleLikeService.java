package software.board.like.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.board.common.outboxmessagerelay.OutboxEventPublisher;
import software.board.common.snowflake.Snowflake;
import software.board.event.EventType;
import software.board.event.payload.ArticleLikedEventPayload;
import software.board.like.entity.ArticleLike;
import software.board.like.entity.ArticleLikeCount;
import software.board.like.repository.ArticleLikeCountRepository;
import software.board.like.repository.ArticleLikeRepository;
import software.board.like.service.response.ArticleLikeResponse;

@Service
@RequiredArgsConstructor
public class ArticleLikeService {

	private final Snowflake snowflake;
	private final ArticleLikeRepository articleLikeRepository;
	private final ArticleLikeCountRepository articleLikeCountRepository;

	private final OutboxEventPublisher outboxEventPublisher;

	@Transactional(readOnly = true)
	public ArticleLikeResponse read(Long articleId, Long userId) {
		return articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
			.map(ArticleLikeResponse::from)
			.orElseThrow(
				() -> new EntityNotFoundException(
					"[ArticleLikeService.read] article like not found.")
			);
	}

	@Transactional
	public void like(Long articleId, Long userId) {
		articleLikeRepository.save(
			ArticleLike.create(
				snowflake.nextId(),
				articleId,
				userId
			)
		);
	}

	@Transactional
	public void unlike(Long articleId, Long userId) {
		articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
			.ifPresent(articleLikeRepository::delete);
	}

	@Transactional
	public void likePessimisticLock1(Long articleId, Long userId) {
		ArticleLike articleLike = articleLikeRepository.save(
			ArticleLike.create(
				snowflake.nextId(),
				articleId,
				userId
			)
		);

		int result = articleLikeCountRepository.increase(articleId);
		if (result == 0) {
			articleLikeCountRepository.save(
				ArticleLikeCount.init(articleId, 1L)
			);
		}

		outboxEventPublisher.publish(
			EventType.ARTICLE_LIKED,
			ArticleLikedEventPayload.builder()
				.articleLikeId(articleLike.getArticleLikeId())
				.articleId(articleLike.getArticleId())
				.userId(articleLike.getUserId())
				.createdAt(articleLike.getCreatedAt())
				.articleLikeCount(count(articleLike.getArticleId()))
				.build(),
			articleLike.getArticleId()
		);
	}

	@Transactional
	public void unlikePessimisticLock1(Long articleId, Long userId) {
		articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
			.ifPresent(articleLike -> {
				articleLikeRepository.delete(articleLike);
				articleLikeCountRepository.decrease(articleId);
				outboxEventPublisher.publish(
					EventType.ARTICLE_UNLIKED,
					ArticleLikedEventPayload.builder()
						.articleLikeId(articleLike.getArticleLikeId())
						.articleId(articleLike.getArticleId())
						.userId(articleLike.getUserId())
						.createdAt(articleLike.getCreatedAt())
						.articleLikeCount(count(articleLike.getArticleId()))
						.build(),
					articleLike.getArticleId()
				);
			});
	}

	@Transactional
	public void likePessimisticLock2(Long articleId, Long userId) {
		ArticleLike articleLike = articleLikeRepository.save(
			ArticleLike.create(
				snowflake.nextId(),
				articleId,
				userId
			)
		);

		// articleLikeCountRepository 에서 찾음 이때 lock 을 획득한다. 없으면 하나 만듦.
		ArticleLikeCount articleLikeCount =
			articleLikeCountRepository.findLockedByArticleId(articleId)
				.orElseGet(() -> ArticleLikeCount.init(articleId, 1L));

		articleLikeCount.increase();
		articleLikeCountRepository.save(articleLikeCount);
	}

	@Transactional
	public void unlikePessimisticLock2(Long articleId, Long userId) {
		articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
			.ifPresent(articleLike -> {
				articleLikeRepository.delete(articleLike);

				ArticleLikeCount articleLikeCount =
					articleLikeCountRepository.findLockedByArticleId(articleId)
						.orElseThrow(() -> new EntityNotFoundException(
							"[ArticleLikeService.unlikePessimisticLock2] article like count not found")
						);
				articleLikeCount.decrease();
			});
	}

	@Transactional
	public void likeOptimisticLock(Long articleId, Long userId) {
		articleLikeRepository.save(
			ArticleLike.create(
				snowflake.nextId(),
				articleId,
				userId
			)
		);

		ArticleLikeCount articleLikeCount = articleLikeCountRepository.findById(articleId)
			.orElseGet(() -> ArticleLikeCount.init(articleId, 0L));
		articleLikeCount.increase();
		articleLikeCountRepository.save(articleLikeCount);
	}

	@Transactional
	public void unlikeOptimisticLock(Long articleId, Long userId) {
		articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
			.ifPresent(articleLike -> {
				articleLikeRepository.delete(articleLike);
				ArticleLikeCount articleLikeCount = articleLikeCountRepository.findById(articleId)
					.orElseThrow(
						() -> new EntityNotFoundException(
							"[ArticleService.unlikeOptimisticLock] article like count not found.")
					);
				articleLikeCount.decrease();
			});
	}

	@Transactional(readOnly = true)
	public Long count(Long articleId) {
		return articleLikeCountRepository.findById(articleId)
			.map(ArticleLikeCount::getLikeCount)
			.orElse(0L);
	}
}
