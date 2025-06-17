package software.board.articleread.cache;

import static java.util.stream.Collectors.joining;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import software.board.dataserializer.DataSerializer;

@Component
@RequiredArgsConstructor
public class OptimizedCacheManager {

	private final StringRedisTemplate redisTemplate;
	private final OptimizedCacheLockProvider optimizedCacheLockProvider;

	private static final String DELIMITER = "::";

	public Object process(String type, long ttlSeconds, Object[] args, Class<?> returnType,
		OptimizedCacheOriginDataSupplier<?> originDataSupplier) throws Throwable {

		String key = generate(type, args);

		// 캐시에 없으면 새로 가져옴 .
		String cachedData = redisTemplate.opsForValue().get(key);
		if (cachedData == null) {
			return refresh(originDataSupplier, key, ttlSeconds);
		}

		OptimizedCache optimizedCache = DataSerializer.deserialize(cachedData,
			OptimizedCache.class);
		if (optimizedCache == null) {
			return refresh(originDataSupplier, key, ttlSeconds);
		}

		if (!optimizedCache.isExpired()) {
			return optimizedCache.parseData(returnType);
		}

		// 내가 캐시를 갱신할 대표인가? (대표이면 lock 을 획득하러감)
		// 대표가 아니면 그냥 낡은 데이터 return 해줌 .
		if (!optimizedCacheLockProvider.lock(key)) {
			return optimizedCache.parseData(returnType);
		}

		try {
			// lock holder 는 origin 데이터를 가져와서 캐시에 갱신
			return refresh(originDataSupplier, key, ttlSeconds);
		} finally {
			optimizedCacheLockProvider.unlock(key);
		}
	}

	private Object refresh(OptimizedCacheOriginDataSupplier<?> originDataSupplier,
		String key, long ttlSeconds) throws Throwable {
		Object result = originDataSupplier.get();

		OptimizedCacheTTL optimizedCacheTTL = OptimizedCacheTTL.of(ttlSeconds);
		OptimizedCache optimizedCache = OptimizedCache.of(result,
			optimizedCacheTTL.getLogicalTTL());

		redisTemplate.opsForValue()
			.set(
				key,
				DataSerializer.serialize(optimizedCache),
				optimizedCacheTTL.getPhysicalTTL() // 저장은 physical ttl로
			);

		return result;
	}

	private String generate(String prefix, Object[] args) {
		return prefix + DELIMITER +
			Arrays.stream(args)
				.map(String::valueOf)
				.collect(joining(DELIMITER));
	}
}
