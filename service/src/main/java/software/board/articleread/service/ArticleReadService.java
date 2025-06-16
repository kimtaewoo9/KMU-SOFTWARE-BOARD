package software.board.articleread.service;

import jakarta.persistence.EntityNotFoundException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.board.article.service.ArticleService;
import software.board.article.service.response.ArticleResponse;
import software.board.articleread.repository.ArticleIdListRepository;
import software.board.articleread.repository.ArticleQueryModel;
import software.board.articleread.repository.ArticleQueryModelRepository;
import software.board.articleread.service.event.handler.EventHandler;
import software.board.articleread.service.response.ArticleReadPageResponse;
import software.board.articleread.service.response.ArticleReadResponse;
import software.board.comment.service.CommentService;
import software.board.event.Event;
import software.board.event.EventPayload;
import software.board.like.service.ArticleLikeService;
import software.board.view.service.ArticleViewService;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleReadService {

	private final ArticleService articleService;
	private final CommentService commentService;
	private final ArticleLikeService articleLikeService;
	private final ArticleViewService articleViewService;

	private final ArticleQueryModelRepository articleQueryModelRepository;

	private final List<EventHandler> eventHandlers;
	private final ArticleIdListRepository articleIdListRepository;

	public void handleEvent(Event<EventPayload> event) {
		for (EventHandler eventHandler : eventHandlers) {
			if (eventHandler.supports(event)) {
				eventHandler.handle(event);
			}
		}
	}

	public ArticleReadResponse read(Long articleId) {
		// articleQueryModel 에서 찾아보고 없으면 fetch
		ArticleQueryModel articleQueryModel = articleQueryModelRepository.read(articleId)
			.or(() -> fetch(articleId))
			.orElseThrow();

		return ArticleReadResponse.from(articleQueryModel, articleViewService.count(articleId));
	}

	private Optional<ArticleQueryModel> fetch(Long articleId) {
		try {
			ArticleResponse articleResponse = articleService.read(articleId);
			ArticleQueryModel articleQueryModel = ArticleQueryModel.create(
				articleResponse,
				commentService.count(articleId),
				articleLikeService.count(articleId)
			);

			articleQueryModelRepository.create(articleQueryModel, Duration.ofDays(3));

			log.info("[ArticleReadService.fetch] fetch data. articleId={}", articleId);
			return Optional.of(articleQueryModel);
		} catch (EntityNotFoundException e) {
			log.warn("[ArticleReadService.fetch] Original article not found. articleId={}",
				articleId);
			return Optional.empty();
		}
	}

	public ArticleReadPageResponse readAll(Long boardId, Long page, Long pageSize) {
		return ArticleReadPageResponse.of(
			readAll(
				readAllArticleIds(boardId, page, pageSize)
			),
			count(boardId)
		);
	}

	private List<ArticleReadResponse> readAll(List<Long> articleIds) {
		Map<Long, ArticleQueryModel> articleQueryModelMap =
			articleQueryModelRepository.readAll(articleIds);

		return articleIds.stream()
			.map(articleId -> articleQueryModelMap.containsKey(articleId) ?
				articleQueryModelMap.get(articleId) :
				fetch(articleId).orElse(null))
			.filter(Objects::nonNull)
			.map(articleQueryModel ->
				ArticleReadResponse.from(
					articleQueryModel,
					articleViewService.count(articleQueryModel.getArticleId())
				))
			.toList();
	}

	private List<Long> readAllArticleIds(Long boardId, Long page, Long pageSize) {
		List<Long> articleIds = articleIdListRepository.readAll(boardId, (page - 1) * pageSize,
			pageSize);
		if (pageSize == articleIds.size()) {
			log.info("[ArticleReadService.readAllArticleIds] return redis data.");
			return articleIds;
		}

		log.info("[ArticleReadService.readAllArticleIds] return origin data.");
		return articleService.readAll(boardId, page, pageSize).getArticles().stream()
			.map(ArticleResponse::getArticleId).toList();
	}

	private Long count(Long boardId) {
		return articleService.count(boardId);
	}

	public List<ArticleReadResponse> readAllInfiniteScroll(Long boardId,
		Long lastArticleId, Long pageSize) {
		return readAll(
			readAllInfiniteScrollArticleIds(boardId, lastArticleId, pageSize)
		);
	}

	private List<Long> readAllInfiniteScrollArticleIds(Long boardId, Long lastArticleId,
		Long pageSize) {
		List<Long> articleIds = articleIdListRepository
			.readAllInfiniteScroll(boardId, lastArticleId, pageSize);

		if (articleIds.size() == pageSize) {
			log.info("[ArticleReadService.readAllInfiniteScrollArticleIds] return redis data.");
			return articleIds;
		}
		log.info("[ArticleReadService.readAllInfiniteScrollArticleIds] return origin data.");
		return articleService.readAllInfiniteScroll(boardId, lastArticleId, pageSize)
			.stream()
			.map(ArticleResponse::getArticleId)
			.toList();
	}
}
