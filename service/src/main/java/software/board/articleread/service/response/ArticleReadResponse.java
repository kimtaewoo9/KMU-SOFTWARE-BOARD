package software.board.articleread.service.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.ToString;
import software.board.articleread.repository.ArticleQueryModel;

@Getter
@ToString
public class ArticleReadResponse {

	@JsonSerialize(using = ToStringSerializer.class)
	private Long articleId;
	private String title;
	private String content;
	@JsonSerialize(using = ToStringSerializer.class)
	private Long boardId;
	@JsonSerialize(using = ToStringSerializer.class)
	private Long writerId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	private Long articleCommentCount;
	private Long articleLikeCount;
	private Long articleViewCount;

	private List<String> fileUrls;

	public static ArticleReadResponse from(ArticleQueryModel articleQueryModel, Long viewCount) {
		ArticleReadResponse response = new ArticleReadResponse();
		response.articleId = articleQueryModel.getArticleId();
		response.title = articleQueryModel.getTitle();
		response.content = articleQueryModel.getContent();
		response.boardId = articleQueryModel.getBoardId();
		response.writerId = articleQueryModel.getWriterId();
		response.createdAt = articleQueryModel.getCreatedAt();
		response.updatedAt = articleQueryModel.getUpdatedAt();

		response.articleCommentCount = articleQueryModel.getArticleCommentCount();
		response.articleLikeCount = articleQueryModel.getArticleLikeCount();
		response.articleViewCount = viewCount;

		response.fileUrls = articleQueryModel.getFileUrls();
		return response;
	}
}
