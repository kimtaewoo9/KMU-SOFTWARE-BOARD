package software.board.articleread.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.RedisZSetCommands.Limit;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ArticleIdListRepository {

	private final StringRedisTemplate redisTemplate;

	private static final String KEY_FORMAT = "article-read::board::%s::article-list";

	public void add(Long boardId, Long articleId, Long limit) {
		redisTemplate.executePipelined((RedisCallback<?>) action -> {
			StringRedisConnection connection = (StringRedisConnection) action;
			String key = generateKey(boardId); // key -> boardId, value -> articleId
			connection.zAdd(key, 0, toPaddedString(articleId));
			connection.zRemRange(key, 0, -limit - 1); // 최신글 1000개 까지만 저장 .
			return null;
		});
	}

	public void delete(Long boardId, Long articleId) {
		redisTemplate.opsForZSet().remove(generateKey(boardId), toPaddedString(articleId));
	}

	public List<Long> readAll(Long boardId, Long offset, Long limit) {
		return redisTemplate.opsForZSet()
			.reverseRange(generateKey(boardId), offset, offset + limit - 1)
			.stream().map(Long::valueOf).toList();
	}

	public List<Long> readAllInfiniteScroll(Long boardId, Long lastArticleId, Long limit) {
		// 사전순 역순으로 범위 조회 .
		return redisTemplate.opsForZSet().reverseRangeByLex(
			generateKey(boardId),
			lastArticleId == null ?
				Range.unbounded() : // 범위 제한 없이 가장 큰 값부터
				Range.leftUnbounded(Range.Bound.exclusive(toPaddedString(lastArticleId))),
			Limit.limit().count(limit.intValue())
		).stream().map(Long::parseLong).toList();
	}

	private String toPaddedString(Long articleId) {
		return "%019d".formatted(articleId);
	}

	private String generateKey(Long boardId) {
		return KEY_FORMAT.formatted(boardId);
	}
}
