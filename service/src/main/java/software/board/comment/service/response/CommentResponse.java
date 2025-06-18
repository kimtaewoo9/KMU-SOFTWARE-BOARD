package software.board.comment.service.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;
import software.board.comment.entity.Comment;

@Getter
@ToString
public class CommentResponse {

	@JsonSerialize(using = ToStringSerializer.class)
	private Long commentId;
	private String content;
	@JsonSerialize(using = ToStringSerializer.class)
	private Long parentCommentId;
	@JsonSerialize(using = ToStringSerializer.class)
	private Long articleId;
	@JsonSerialize(using = ToStringSerializer.class)
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
