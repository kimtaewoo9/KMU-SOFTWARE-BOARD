package software.board.comment.api;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
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

	@Test
	void readAll() {
		CommentPageResponse commentPageResponse = restClient.get()
			.uri("/v1/comments?articleId=1&page=1&pageSize=20")
			.retrieve()
			.body(CommentPageResponse.class);

		List<CommentResponse> comments = commentPageResponse.getComments();
		for (CommentResponse comment : comments) {
			System.out.println("commentId=" + comment.getCommentId());
		}

		/*
		[FIRST PAGE]
		commentId=190766997754535936
		commentId=190766997821644840
		commentId=190766997754535937
		commentId=190766997813256193
		commentId=190766997754535938
		commentId=190766997813256196
		commentId=190766997754535939
		commentId=190766997821644806
		commentId=190766997754535940
		commentId=190766997821644845
		[NEXT PAGE]
		commentId=190766997771313152
		commentId=190766997813256192
		commentId=190766997771313153
		commentId=190766997821644841
		commentId=190766997771313154
		commentId=190766997813256195
		commentId=190766997771313155
		commentId=190766997817450506
		commentId=190766997792284672
		commentId=190766997813256194
		 */
	}

	@Test
	void readAllInfiniteScrollTest() {
		List<CommentResponse> response = restClient.get()
			.uri("/v1/comments/infinite-scroll?articleId=1&pageSize=10")
			.retrieve()
			.body(new ParameterizedTypeReference<List<CommentResponse>>() {
			});

		System.out.println("[FIRST PAGE]");
		for (CommentResponse commentResponse : response) {
			System.out.println("commentId=" + commentResponse.getCommentId());
		}

		Long lastParentCommentId = response.getLast().getParentCommentId();
		Long lastCommentId = response.getLast().getCommentId();

		List<CommentResponse> response2 = restClient.get()
			.uri(
				"/v1/comments/infinite-scroll?articleId=1&lastParentCommentId=%s&lastCommentId=%s&pageSize=10"
					.formatted(lastParentCommentId, lastCommentId))
			.retrieve()
			.body(new ParameterizedTypeReference<List<CommentResponse>>() {
			});

		System.out.println("[NEXT PAGE]");
		for (CommentResponse commentResponse : response2) {
			System.out.println("commentId=" + commentResponse.getCommentId());
		}
	}

	@Test
	void countTest() {
		CommentCreateRequest request = new CommentCreateRequest(1L, "content", null, 1L);

		CommentResponse response = restClient.post()
			.uri("/v1/comments")
			.body(request)
			.retrieve()
			.body(CommentResponse.class);

		Long count = restClient.get()
			.uri("/v1/comments/articles/{articleId}/count", response.getArticleId())
			.retrieve()
			.body(Long.class);

		System.out.println("count: " + count);

		restClient.delete()
			.uri("/v1/comments/{commentId}", response.getCommentId())
			.retrieve()
			.body(Void.class);

		Long count2 = restClient.get()
			.uri("/v1/comments/articles/{articleId}/count", response.getArticleId())
			.retrieve()
			.body(Long.class);

		System.out.println("count2: " + count2);
	}

	@Getter
	static class CommentPageResponse {

		List<CommentResponse> comments;
		Long commentCount;
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
