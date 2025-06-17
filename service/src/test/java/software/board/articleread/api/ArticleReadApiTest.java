package software.board.articleread.api;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import software.board.article.service.response.ArticlePageResponse;
import software.board.article.service.response.ArticleResponse;
import software.board.articleread.service.response.ArticleReadPageResponse;
import software.board.articleread.service.response.ArticleReadResponse;

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

	@Test
	void readAllTest() {
		ArticleReadPageResponse response = restClient.get()
			.uri("/v1/articles?boardId=%s&page=%s&pageSize=%s".formatted(1L, 1L, 5L))
			.retrieve()
			.body(ArticleReadPageResponse.class);

		List<ArticleReadResponse> articles = response.getArticles();
		System.out.println("response.getArticleCount()=" + response.getArticleCount());
		for (ArticleReadResponse article : articles) {
			System.out.println("articleId=" + article.getArticleId());
		}

		ArticlePageResponse response2 = restClient.get()
			.uri("/v1/articles/source?boardId=%s&page=%s&pageSize=%s".formatted(1L, 1L, 5L))
			.retrieve()
			.body(ArticlePageResponse.class);

		System.out.println("response2.getArticleCount()=" + response2.getArticleCount());
		for (ArticleReadResponse article : articles) {
			System.out.println("articleId=" + article.getArticleId());
		}
	}

	@Test
	void readAllInfiniteScrollTest() {
		List<ArticleReadResponse> response = restClient.get()
			.uri("/v1/articles/infinite-scroll?boardId=%s&pageSize=%s&lastArticleId=%s"
				.formatted(1L, 5L, 192946982400946176L))
			.retrieve()
			.body(new ParameterizedTypeReference<List<ArticleReadResponse>>() {
			});
		System.out.println("response.size()=" + response.size());
		for (ArticleReadResponse articleReadResponse : response) {
			System.out.println("articleId=" + articleReadResponse.getArticleId());
		}

		List<ArticleResponse> response2 = restClient.get()
			.uri("/v1/articles/infinite-scroll/source?boardId=%s&pageSize=%s&lastArticleId=%s"
				.formatted(1L, 5L, 192946982400946176L))
			.retrieve()
			.body(new ParameterizedTypeReference<List<ArticleResponse>>() {
			});
		System.out.println("response2.size()=" + response2.size());
		for (ArticleResponse articleResponse : response2) {
			System.out.println("articleId=" + articleResponse.getArticleId());
		}

	}

}
