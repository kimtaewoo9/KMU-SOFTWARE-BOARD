package software.board.hotarticle.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.board.article.service.ArticleService;
import software.board.article.service.response.ArticleResponse;

@Component
@RequiredArgsConstructor
@Slf4j
public class ArticleClient {

	private final ArticleService articleService;

	public ArticleResponse read(Long articleId) {
		try {
			return articleService.read(articleId);
		} catch (Exception e) {
			log.error("[ArticleClient.read] articleId={}", articleId, e);
		}
		return null;
	}
}
