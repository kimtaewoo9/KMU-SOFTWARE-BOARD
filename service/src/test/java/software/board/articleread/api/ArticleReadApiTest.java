package software.board.articleread.api;

import java.time.LocalDateTime;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

public class ArticleReadApiTest {

	RestClient restClient = RestClient.create("http://localhost:8080");

	@Test
	void readTest() {

		// redis 에서 가져오는 경우
		ArticleReadResponse response = restClient.get()
			.uri("/v1/articles/{articleId}", 192912706650574848L)
			.retrieve()
			.body(ArticleReadResponse.class);

		System.out.println("response=" + response);

		// article service 를 통해 article repository 에서 fetch 를 통해 가져오는 경우 .
		ArticleReadResponse response2 = restClient.get()
			.uri("/v1/articles/{articleId}", 190385455880114176L)
			.retrieve()
			.body(ArticleReadResponse.class);

		System.out.println("response2=" + response);
	}

	@Data
	static class ArticleReadResponse {

		private Long articleId;
		private String title;
		private String content;
		private Long boardId;
		private Long writerId;
		private LocalDateTime createdAt;
		private LocalDateTime modifiedAt;
		private Long articleCommentCount;
		private Long articleLikeCount;
		private Long articleViewCount;

	}
}
