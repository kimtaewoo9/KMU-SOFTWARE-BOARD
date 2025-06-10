package software.board.comment.api;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

public class CommentApiTest {

	RestClient restClient = RestClient.create("http://localhost:8080");

	@Test
	void create() {
		CommentResponse response1 =
			createComment(new CommentCreateRequest(1L, "댓글 1", null, 1L));
		CommentResponse response2 =
			createComment(new CommentCreateRequest(1L, "댓글 2", response1.getCommentId(), 1L));
		CommentResponse response3 =
			createComment(new CommentCreateRequest(1L, "댓글 3", response1.getCommentId(), 1L));

		System.out.println("commentId=%s".formatted(response1.getCommentId()));
		System.out.println("\tcommentId=%s".formatted(response2.getCommentId()));
		System.out.println("\tcommentId=%s".formatted(response3.getCommentId()));
	}

	@Test
	void readTest() {
		CommentResponse response = read(190761095999188992L);

		System.out.println("response=" + response);
	}

	private CommentResponse read(Long commentId) {
		return restClient.get()
			.uri("/v1/comments/{commentId}", commentId)
			.retrieve()
			.body(CommentResponse.class);
	}

	@Test
	void updateTest() {
		CommentUpdateRequest request = new CommentUpdateRequest("[updated comment]");

		CommentResponse response = restClient.put()
			.uri("/v1/comments/{commentId}", 190761095999188992L)
			.body(request)
			.retrieve()
			.body(CommentResponse.class);

		System.out.println("response: " + response);
		System.out.println("response.getContent()" + response.getContent());
	}

	@Test
	void deleteTest() {
		restClient.delete()
			.uri("/v1/comments/{commentId}", 190761095999188992L)
			.retrieve()
			.body(Void.class);
	}

	CommentResponse createComment(CommentCreateRequest request) {
		return restClient.post()
			.uri("/v1/comments")
			.body(request)
			.retrieve()
			.body(CommentResponse.class);
	}

	@Data
	@AllArgsConstructor
	static class CommentUpdateRequest {

		private String content;
	}

	@Data
	@AllArgsConstructor
	static class CommentCreateRequest {

		private Long articleId;
		private String content;
		private Long parentCommentId;
		private Long writerId;
	}

	@Data
	static class CommentResponse {

		private Long commentId;
		private String content;
		private Long parentCommentId;
		private Long articleId;
		private Long writerId;
		private boolean deleted;
		private LocalDateTime createdAt;
	}
}
