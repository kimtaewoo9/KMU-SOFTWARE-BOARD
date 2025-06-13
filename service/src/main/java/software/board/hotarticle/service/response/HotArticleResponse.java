package software.board.hotarticle.service.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;
import software.board.article.service.response.ArticleResponse;

@Getter
@ToString
public class HotArticleResponse {

	private Long articleId;
	private String title;
	private LocalDateTime createdAt;

	public static HotArticleResponse from(ArticleResponse articleResponse) {
		HotArticleResponse response = new HotArticleResponse();
		response.articleId = articleResponse.getArticleId();
		response.title = articleResponse.getTitle();
		response.createdAt = articleResponse.getCreatedAt();

		return response;
	}
}
