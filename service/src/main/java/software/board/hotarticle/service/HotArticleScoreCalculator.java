package software.board.hotarticle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.board.hotarticle.repository.ArticleCommentCountRepository;
import software.board.hotarticle.repository.ArticleLikeCountRepository;
import software.board.hotarticle.repository.ArticleViewCountRepository;

@Component
@RequiredArgsConstructor
public class HotArticleScoreCalculator {

	// TODO 좋아요가 10 개 이상이면 hot article 인걸로 변경 .
	private final ArticleLikeCountRepository articleLikeCountRepository;
	private final ArticleViewCountRepository articleViewCountRepository;
	private final ArticleCommentCountRepository articleCommentCountRepository;

	private static final long ARTICLE_LIKE_COUNT_WEIGHT = 10;
	private static final long ARTICLE_VIEW_COUNT_WEIGHT = 1;
	private static final long ARTICLE_COMMENT_COUNT_WEIGHT = 1;

	public long calculate(Long articleId) {
		Long articleLikeCount = articleLikeCountRepository.read(articleId);
		Long articleViewCount = articleViewCountRepository.read(articleId);
		Long articleCommentCount = articleCommentCountRepository.read(articleId);

		return articleLikeCount * ARTICLE_LIKE_COUNT_WEIGHT +
			articleViewCount * ARTICLE_VIEW_COUNT_WEIGHT +
			articleCommentCount * ARTICLE_COMMENT_COUNT_WEIGHT;
	}
}
