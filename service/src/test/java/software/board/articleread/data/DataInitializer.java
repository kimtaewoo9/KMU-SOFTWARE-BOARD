package software.board.articleread.data;

import java.time.LocalDateTime;
import java.util.random.RandomGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

public class DataInitializer {

	RestClient restClient = RestClient.create("http://localhost:8080");

	@Test
	void initialize() {
		for (int i = 0; i < 30; i++) {
			Long articleId = createArticleAndReturnArticleId();

			long commentCount = RandomGenerator.getDefault().nextLong(10);
			long likeCount = RandomGenerator.getDefault().nextLong(100);
			long viewCount = RandomGenerator.getDefault().nextLong(200);

			createComment(articleId, commentCount);
			like(articleId, likeCount);
			view(articleId, viewCount);
		}
	}

	private Long createArticleAndReturnArticleId() {
		LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

		ArticleCreateRequest request =
			new ArticleCreateRequest("title", "content", 1L, 1L);
		body.add("request", request);

		return restClient.post()
			.uri("/v1/articles")
			.body(body)
			.retrieve()
			.body(ArticleResponse.class)
			.getArticleId();
	}

	@Data
	@AllArgsConstructor
	static class ArticleCreateRequest {

		private String title;
		private String content;
		private Long boardId;
		private Long writerId;
	}

	@Data
	static class ArticleResponse {

		private Long articleId;
		private String title;
		private String content;
		private Long boardId;
		private Long writerId;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;
	}

	private void createComment(Long articleId, long commentCount) {
		long count = commentCount;
		while (count-- != 0) {
			restClient.post()
				.uri("/v1/comments")
				.body(new CreateCommentRequest(articleId, "content", 3L))
				.retrieve()
				.body(CommentResponse.class);
		}
	}

	@Data
	@AllArgsConstructor
	static class CreateCommentRequest {

		private Long articleId;
		private String content;
		private Long writerId;
	}

	@Data
	static class CommentResponse {

		private Long commentId;
	}

	private void like(Long articleId, long likeCount) {
		long count = likeCount;
		while (count-- != 0) {
			restClient.post()
				.uri("/v1/article-likes/articles/{articleId}/users/{userId}/pessimistic-lock-1",
					articleId, count)
				.retrieve()
				.body(Long.class);
		}
	}

	private void view(Long articleId, long viewCount) {
		long count = viewCount;
		while (count-- != 0) {
			restClient.post()
				.uri("/v1/article-views/articles/{articleId}", viewCount)
				.retrieve()
				.body(Long.class);
		}
	}
}
