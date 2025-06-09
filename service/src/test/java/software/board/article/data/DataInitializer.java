package software.board.article.data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kuke.board.common.snowflake.Snowflake;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;
import software.board.article.entity.Article;

@SpringBootTest
public class DataInitializer {

	@PersistenceContext
	EntityManager entityManager;
	@Autowired
	TransactionTemplate transactionTemplate;
	@Autowired
	Snowflake snowflake;

	CountDownLatch latch = new CountDownLatch(EXECUTE_COUNT);

	static final int BULK_INSERT_SIZE = 2000;
	static final int EXECUTE_COUNT = 6000;

	@Test
	void initialized() throws InterruptedException {
		long startTime = System.currentTimeMillis(); // 시작 시간 기록

		ExecutorService executorService = Executors.newFixedThreadPool(10);
		for (int i = 0; i < EXECUTE_COUNT; i++) {
			executorService.submit(() -> {
				insert();
				latch.countDown();
				System.out.println("latch.getCount(): " + latch.getCount());
			});
		}
		latch.await(); // main 스레드는 6000번의 작업이 끝날때까지 기다림.
		executorService.shutdown();

		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime - startTime;

		System.out.println("✅ 총 소요 시간: " + elapsedTime + "ms (" + (elapsedTime / 1000.0) + "초)");
	}

	void insert() {
		// insert 작업은 하나의 데이터베이스 트랜잭션으로 묶여서 처리 되도록 보장함 . (insert -> 스레드 풀에 제출할 작업)
		transactionTemplate.executeWithoutResult(status -> {
			for (int i = 0; i < BULK_INSERT_SIZE; i++) {
				Article article = Article.create(
					snowflake.nextId(),
					"title" + i,
					"content" + i,
					1L,
					1L
				);
				entityManager.persist(article);
			}
		});
	}
}
