package software.board.hotarticle.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HotArticleListRepositoryTest {

	@Autowired
	HotArticleListRepository hotArticleListRepository;

	@Test
	void addTest() throws InterruptedException {

		// given
		LocalDateTime time = LocalDateTime.of(2025, 6, 12, 0, 0);
		long limit = 3;

		hotArticleListRepository.add(1L, time, 2L, limit, Duration.ofSeconds(3));
		hotArticleListRepository.add(2L, time, 3L, limit, Duration.ofSeconds(3));
		hotArticleListRepository.add(3L, time, 1L, limit, Duration.ofSeconds(3));
		hotArticleListRepository.add(4L, time, 5L, limit, Duration.ofSeconds(3));
		hotArticleListRepository.add(5L, time, 4L, limit, Duration.ofSeconds(3));
		// 4번 5번 2번만 남아야함

		List<Long> articleIds = hotArticleListRepository.readAll("20250612");

		assertThat(articleIds).hasSize(3);

		assertThat(articleIds.get(0)).isEqualTo(4);
		assertThat(articleIds.get(1)).isEqualTo(5);
		assertThat(articleIds.get(2)).isEqualTo(2);

		TimeUnit.SECONDS.sleep(5);

		assertThat(hotArticleListRepository.readAll("20240723")).isEmpty();
	}
}
