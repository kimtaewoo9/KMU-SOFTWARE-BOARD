package software.board.article.service.response;

import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.ToString;
import software.board.article.entity.Article;

@Getter
@ToString
public class ArticleResponse {

	private String articleId;
	private String title;
	private String content;
	private Long boardId;
	private Long writerId;
	private String createdAt;
	private String updatedAt;
	private List<String> fileUrls;

	public static ArticleResponse from(Article article, List<String> fileUrls) {

		ArticleResponse articleResponse = new ArticleResponse();
		articleResponse.articleId = String.valueOf(article.getArticleId());
		articleResponse.title = article.getTitle();
		articleResponse.content = article.getContent();
		articleResponse.boardId = article.getBoardId();
		articleResponse.writerId = article.getWriterId();
		articleResponse.createdAt = String.valueOf(article.getCreatedAt());
		articleResponse.updatedAt = String.valueOf(article.getUpdatedAt());
		articleResponse.fileUrls = fileUrls;

		return articleResponse;
	}

	public static ArticleResponse from(Article article) {
		return from(article, Collections.emptyList());
	}
}
