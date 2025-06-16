package software.board.articleread.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ArticleIdListRepository {

	private final StringRedisTemplate redisTemplate;

	private static final String KEY_FORMAT = "article-read::board::%s::article-list";

	public void add() {

	}
}
