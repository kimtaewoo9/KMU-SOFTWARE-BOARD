package software.board.article.api;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

class ArticleApiTest {

	RestClient restClient = RestClient.create("http://localhost:8080");

	@Test
	void createTest() {
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

		ArticleCreateRequest requestDto =
			new ArticleCreateRequest("title", "content", 1L, 1L);
		body.add("request", requestDto);

		ArticleResponse response = restClient.post()
			.uri("/v1/articles")
			.body(body)
			.retrieve()
			.body(ArticleResponse.class);
		System.out.println(response);
	}

	@Test
	void updateTest() {
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

		ArticleUpdateRequest articleUpdateRequest =
			new ArticleUpdateRequest("new title", "new content");
		body.add("request", articleUpdateRequest);

		ArticleResponse response = update(190386755531345920L, body);
		System.out.println(response);
	}

	ArticleResponse update(Long articleId, MultiValueMap<String, Object> body) {
		return restClient.put()
			.uri("/v1/articles/{articleId}", articleId)
			.body(body)
			.retrieve()
			.body(ArticleResponse.class);
	}

	@Test
	void deleteTest() {
		Long articleId = 190386755531345920L;
		ResponseEntity<Void> response = restClient.delete()
			.uri("/v1/articles/{articleId}", articleId)
			.retrieve()
			.toBodilessEntity();
	}

	@Test
	void readAllTest() {
		ArticlePageResponse articlePageResponse = restClient.get()
			.uri("/v1/articles?boardId=1&page=1&pageSize=10")
			.retrieve()
			.body(ArticlePageResponse.class);

		System.out.println(
			"articlePageResponse.getArticleCount(): " + articlePageResponse.getArticleCount());
		for (ArticleResponse article : articlePageResponse.getArticles()) {
			System.out.println("articleId: " + article.getArticleId());
		}
	}

	@Test
	void readAllInfiniteScroll() {
		List<ArticleResponse> firstPage = restClient.get()
			.uri("/v1/articles/infinite-scroll?boardId=1&pageSize=10")
			.retrieve()
			.body(new ParameterizedTypeReference<List<ArticleResponse>>() {
			});

		System.out.println("[FIRST PAGE]");
		for (ArticleResponse articleResponse : firstPage) {
			System.out.println("articleId=" + articleResponse.getArticleId());
		}

		String lastArticleId = firstPage.getLast().getArticleId();
		List<ArticleResponse> nextPage = restClient.get()
			.uri("/v1/articles/infinite-scroll?boardId=1&pageSize=10&lastArticleId=%s".formatted(
				lastArticleId))
			.retrieve()
			.body(new ParameterizedTypeReference<List<ArticleResponse>>() {
			});

		System.out.println("[NEXT PAGE}");
		for (ArticleResponse articleResponse : nextPage) {
			System.out.println("articleId=" + articleResponse.getArticleId());
		}
	}

	@Data
	static class ArticlePageResponse {

		private List<ArticleResponse> articles;
		private Long articleCount;
	}

	@Data
	@AllArgsConstructor
	static class ArticleUpdateRequest {

		private String title;
		private String content;
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

		private String articleId;
		private String title;
		private String content;
		private String boardId;
		private String writerId;
		private String createdAt;
		private String updatedAt;

		private List<String> fileUrls;
	}
}
