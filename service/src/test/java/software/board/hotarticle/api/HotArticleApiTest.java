package software.board.hotarticle.api;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

public class HotArticleApiTest {

	RestClient restClient = RestClient.create("http://localhost:8080");

	@Test
	void readAllTest() {
		List<HotArticleResponse> hotArticleResponseList = restClient.get()
			.uri("/v1/hot-articles/articles/date/{dateStr}", "20250614")
			.retrieve()
			.body(new ParameterizedTypeReference<List<HotArticleResponse>>() {
			});

		for (HotArticleResponse hotArticleResponse : hotArticleResponseList) {
			System.out.println("hotArticleResponse:" + hotArticleResponse);
		}
	}

	@Data
	static class HotArticleResponse {

		private Long articleId;
		private String title;
		private LocalDateTime createdAt;
	}
}
