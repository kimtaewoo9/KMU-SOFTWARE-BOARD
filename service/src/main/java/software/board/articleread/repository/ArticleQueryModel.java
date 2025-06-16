package software.board.articleread.repository;

import java.time.LocalDateTime;
import lombok.Getter;
import software.board.article.service.response.ArticleResponse;
import software.board.event.payload.ArticleCreatedEventPayload;
import software.board.event.payload.ArticleLikedEventPayload;
import software.board.event.payload.ArticleUnlikedEventPayload;
import software.board.event.payload.ArticleUpdatedEventPayload;
import software.board.event.payload.CommentCreatedEventPayload;
import software.board.event.payload.CommentDeletedEventPayload;

@Getter
public class ArticleQueryModel {

	private Long articleId;
	private String title;
	private String content;
	private Long boardId;
	private Long writerId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	private Long articleCommentCount;
	private Long articleLikeCount;

	// 이벤트 기반으로 query model 생성
	public static ArticleQueryModel create(ArticleCreatedEventPayload payload) {
		ArticleQueryModel articleQueryModel = new ArticleQueryModel();

		articleQueryModel.articleId = payload.getArticleId();
		articleQueryModel.title = payload.getTitle();
		articleQueryModel.content = payload.getContent();
		articleQueryModel.boardId = payload.getBoardId();
		articleQueryModel.writerId = payload.getWriterId();
		articleQueryModel.createdAt = payload.getCreatedAt();
		articleQueryModel.updatedAt = payload.getUpdatedAt();

		articleQueryModel.articleCommentCount = 0L;
		articleQueryModel.articleLikeCount = 0L;
		return articleQueryModel;
	}

	public static ArticleQueryModel create(ArticleResponse articleResponse,
		Long commentCount, Long likeCount) {
		ArticleQueryModel articleQueryModel = new ArticleQueryModel();

		articleQueryModel.articleId = articleResponse.getArticleId();
		articleQueryModel.title = articleResponse.getTitle();
		articleQueryModel.content = articleResponse.getContent();
		articleQueryModel.boardId = articleResponse.getBoardId();
		articleQueryModel.writerId = articleResponse.getWriterId();
		articleQueryModel.createdAt = articleResponse.getCreatedAt();
		articleQueryModel.updatedAt = articleResponse.getUpdatedAt();

		articleQueryModel.articleCommentCount = commentCount;
		articleQueryModel.articleLikeCount = likeCount;
		return articleQueryModel;
	}

	// 댓글 생성, 삭제 이벤트 시 updatedBy 호출
	public void updateBy(CommentCreatedEventPayload payload) {
		this.articleCommentCount = payload.getArticleCommentCount();
	}

	public void updateBy(CommentDeletedEventPayload payload) {
		this.articleCommentCount = payload.getArticleCommentCount();
	}

	public void updateBy(ArticleLikedEventPayload payload) {
		this.articleLikeCount = payload.getArticleLikeCount();
	}

	public void updateBy(ArticleUnlikedEventPayload payload) {
		this.articleLikeCount = payload.getArticleLikeCount();
	}

	public void updateBy(ArticleUpdatedEventPayload payload) {
		this.title = payload.getTitle();
		this.content = payload.getContent();
		this.boardId = payload.getBoardId();
		this.writerId = payload.getWriterId();
		this.createdAt = payload.getCreatedAt();
		this.updatedAt = payload.getUpdatedAt();
	}
}
