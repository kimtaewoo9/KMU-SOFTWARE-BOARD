package software.board.article.service.response;

import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ArticlePageResponse {

	private List<ArticleResponse> articles;
	private Long articleCount;

	public static ArticlePageResponse of(List<ArticleResponse> articles, Long articleCount) {
		ArticlePageResponse articlePageResponse = new ArticlePageResponse();
		articlePageResponse.articles = articles;
		articlePageResponse.articleCount = articleCount;

		return articlePageResponse;
	}
}
