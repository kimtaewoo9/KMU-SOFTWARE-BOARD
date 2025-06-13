package software.board.hotarticle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.board.hotarticle.repository.HotArticleCommentCountRepository;
import software.board.hotarticle.repository.HotArticleLikeCountRepository;
import software.board.hotarticle.repository.HotArticleViewCountRepository;

@Component
@RequiredArgsConstructor
public class HotArticleScoreCalculator {

	// TODO 좋아요가 10 개 이상이면 hot article 인걸로 변경 .
	private final HotArticleLikeCountRepository hotArticleLikeCountRepository;
	private final HotArticleViewCountRepository hotArticleViewCountRepository;
	private final HotArticleCommentCountRepository hotArticleCommentCountRepository;

	private static final long ARTICLE_LIKE_COUNT_WEIGHT = 10;
	private static final long ARTICLE_VIEW_COUNT_WEIGHT = 1;
	private static final long ARTICLE_COMMENT_COUNT_WEIGHT = 1;

	public long calculate(Long articleId) {
		Long articleLikeCount = hotArticleLikeCountRepository.read(articleId);
		Long articleViewCount = hotArticleViewCountRepository.read(articleId);
		Long articleCommentCount = hotArticleCommentCountRepository.read(articleId);

		return articleLikeCount * ARTICLE_LIKE_COUNT_WEIGHT +
			articleViewCount * ARTICLE_VIEW_COUNT_WEIGHT +
			articleCommentCount * ARTICLE_COMMENT_COUNT_WEIGHT;
	}
}
