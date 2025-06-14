package software.board.hotarticle.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.board.like.entity.ArticleLikeCount;
import software.board.like.repository.ArticleLikeCountRepository;

@Component
@RequiredArgsConstructor
public class HotArticleScoreCalculator {

	// TODO 좋아요가 10 개 이상이면 hot article 인걸로 변경 .
	private final ArticleLikeCountRepository articleLikeCountRepository;

	private static final long ARTICLE_LIKE_COUNT_WEIGHT = 10;

	public long calculate(Long articleId) {

		ArticleLikeCount likeCount = articleLikeCountRepository.findLockedByArticleId(articleId)
			.orElseThrow(() -> new EntityNotFoundException(
				"[HotArticleScoreCalculator.ArticleLikeCountRepository] article like not found."));

		return likeCount.getLikeCount();
	}
}
