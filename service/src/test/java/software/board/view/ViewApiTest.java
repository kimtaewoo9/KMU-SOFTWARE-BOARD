package software.board.view;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

public class ViewApiTest {

	RestClient restClient = RestClient.create("http://localhost:8080");

	@Test
	void viewTest() throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(100);
		CountDownLatch latch = new CountDownLatch(10000);

		for (int cnt_i = 0; cnt_i < 10000; cnt_i++) {
			executorService.submit(() -> {
				restClient.post()
					.uri("/v1/article-views/articles/{articleId}", 1L)
					.retrieve()
					.body(Long.class);
				latch.countDown();
			});
		}

		latch.await();

		Long count = restClient.get()
			.uri("/v1/article-views/articles/{articleId}/count", 1L)
			.retrieve()
			.body(Long.class);

		System.out.println("count: " + count);
	}
}
