package software.board.view.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ArticleViewCountRepository {

	private final StringRedisTemplate redisTemplate;

	public static final String KEY_FORMAT = "view::article::%s::view_count";

	public Long read(Long articleId) {
		String result = redisTemplate.opsForValue().get(generateKey(articleId));

//		log.info("✅ key= %s, value= %s".formatted(generateKey(articleId), result));
		return result == null ? 0L : Long.parseLong(result);
	}

	public Long increase(Long articleId) {
		return redisTemplate.opsForValue().increment(generateKey(articleId));
	}

	private String generateKey(Long articleId) {
		return KEY_FORMAT.formatted(articleId);
	}
}
