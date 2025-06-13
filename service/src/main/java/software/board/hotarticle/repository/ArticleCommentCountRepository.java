package software.board.hotarticle.repository;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ArticleCommentCountRepository {

	private final StringRedisTemplate redisTemplate;

	// hot-article::article::{articleId}::comment-count
	private static final String KEY_FORMAT = "hot-article::article::%s::comment-count";

	public void createOrUpdate(Long articleId, Long commentCount, Duration ttl) {
		redisTemplate.opsForValue().set(generateKey(articleId), String.valueOf(commentCount));
	}

	public Long read(Long articleId) {
		String result = redisTemplate.opsForValue().get(generateKey(articleId));
		return result == null ? 0L : Long.parseLong(result);
	}

	private String generateKey(Long articleId) {
		return KEY_FORMAT.formatted(articleId);
	}
}
