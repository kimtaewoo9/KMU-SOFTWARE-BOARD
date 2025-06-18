package software.board.like.service.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;
import software.board.like.entity.ArticleLike;

@Getter
@ToString
public class ArticleLikeResponse {

	@JsonSerialize(using = ToStringSerializer.class)
	private Long articleLikeId;
	@JsonSerialize(using = ToStringSerializer.class)
	private Long articleId;
	@JsonSerialize(using = ToStringSerializer.class)
	private Long userId;
	private LocalDateTime createdAt;

	public static ArticleLikeResponse from(ArticleLike articleLike) {
		ArticleLikeResponse response = new ArticleLikeResponse();
		response.articleLikeId = articleLike.getArticleLikeId();
		response.articleId = articleLike.getArticleId();
		response.userId = articleLike.getUserId();
		response.createdAt = articleLike.getCreatedAt();
		return response;
	}
}
