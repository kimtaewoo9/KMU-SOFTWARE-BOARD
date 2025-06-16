package software.board.articleread.repository;

import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toMap;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import software.board.dataserializer.DataSerializer;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ArticleQueryModelRepository {

	private final StringRedisTemplate redisTemplate;

	// article-read::article::{articleId}
	private static final String KEY_FORMAT = "article-read::article::%s";

	public void create(ArticleQueryModel articleQueryModel, Duration ttl) {
		redisTemplate.opsForValue().set(
			generateKey(articleQueryModel.getArticleId()),
			DataSerializer.serialize(articleQueryModel),
			ttl);
	}

	public void update(ArticleQueryModel articleQueryModel) {
		redisTemplate.opsForValue().setIfPresent(
			generateKey(articleQueryModel.getArticleId()),
			DataSerializer.serialize(articleQueryModel)
		);
	}

	public void delete(Long articleId) {
		redisTemplate.delete(generateKey(articleId));
	}

	public Optional<ArticleQueryModel> read(Long articleId) {
		return Optional.ofNullable(redisTemplate.opsForValue().get(generateKey(articleId)))
			.map(json -> DataSerializer.deserialize(json, ArticleQueryModel.class));
	}

	public Map<Long, ArticleQueryModel> readAll(List<Long> articleIds) {
		List<String> keyList = articleIds.stream().map(this::generateKey).toList();

		return redisTemplate.opsForValue().multiGet(keyList).stream()
			.map(json -> DataSerializer.deserialize(json, ArticleQueryModel.class))
			.collect(toMap(ArticleQueryModel::getArticleId, identity()));
	}

	private String generateKey(Long articleId) {
		return KEY_FORMAT.formatted(articleId);
	}
}
