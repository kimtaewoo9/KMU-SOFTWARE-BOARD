package software.board.article.service.response;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.ToString;
import software.board.article.entity.Article;

@Getter
@ToString
public class ArticleResponse {

	private Long articleId;
	private String title;
	private String content;
	private Long boardId;
	private Long writerId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private List<String> fileUrls;

	public static ArticleResponse from(Article article, List<String> fileUrls) {

		ArticleResponse articleResponse = new ArticleResponse();
		articleResponse.articleId = article.getArticleId();
		articleResponse.title = article.getTitle();
		articleResponse.content = article.getContent();
		articleResponse.boardId = article.getBoardId();
		articleResponse.writerId = article.getWriterId();
		articleResponse.createdAt = article.getCreatedAt();
		articleResponse.updatedAt = article.getUpdatedAt();
		articleResponse.fileUrls = fileUrls;

		return articleResponse;
	}

	public static ArticleResponse from(Article article) {
		return from(article, Collections.emptyList());
	}
}
