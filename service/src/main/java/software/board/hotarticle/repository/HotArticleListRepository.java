package software.board.hotarticle.repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class HotArticleListRepository {

	private final StringRedisTemplate redisTemplate;

	// hot-article::list::%s
	private static final String KEY_FORMAT = "hot-article::list::%s";

	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

	public void add(Long articleId, LocalDateTime time, Long score, Long limit, Duration ttl) {
		// limit -> 인기글은 몇개를 유지할지 ..
		redisTemplate.executePipelined((RedisCallback<?>) action -> {
			StringRedisConnection connection = (StringRedisConnection) action;
			String key = generateKey(time);
			connection.zAdd(key, score, String.valueOf(articleId)); // key, 정렬 기준, 값
			connection.zRemRange(key, 0, -limit - 1); // add 에서 몇개 유지할지 정함 .
			connection.expire(key, ttl.toSeconds());
			return null;
		});
	}

	public void remove(LocalDateTime dateStr, Long articleId) {
		// key -> 날짜, value -> articleId
		redisTemplate.opsForZSet().remove(generateKey(dateStr), String.valueOf(articleId));
	}

	private String generateKey(LocalDateTime time) {
		return generateKey(TIME_FORMATTER.format(time));
	}

	private String generateKey(String dateStr) {
		return KEY_FORMAT.formatted(dateStr);
	}

	public List<Long> readAll(String dateStr) {
		return redisTemplate.opsForZSet()
			.reverseRangeWithScores(generateKey(dateStr), 0, -1).stream()
			.peek(tuple ->
				log.info("[HotArticleListRepository.readAll] articleId={}, score={}",
					tuple.getValue(), tuple.getScore()))
			.map(ZSetOperations.TypedTuple::getValue)
			.map(Long::valueOf)
			.toList();
	}
}
