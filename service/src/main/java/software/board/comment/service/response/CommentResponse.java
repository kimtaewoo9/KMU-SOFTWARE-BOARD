package software.board.comment.service.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;
import software.board.comment.entity.Comment;

@Getter
@ToString
public class CommentResponse {

	private Long commentId;
	private String content;
	private Long parentCommentId;
	private Long articleId;
	private Long writerId;
	private boolean deleted;
	private LocalDateTime createdAt;

	public static CommentResponse from(Comment comment) {
		CommentResponse response = new CommentResponse();
		response.commentId = comment.getCommentId();
		response.content = comment.getContent();
		response.parentCommentId = comment.getParentCommentId();
		response.articleId = comment.getArticleId();
		response.writerId = comment.getWriterId();
		response.deleted = comment.getDeleted();
		response.createdAt = comment.getCreatedAt();

		return response;
	}
}
