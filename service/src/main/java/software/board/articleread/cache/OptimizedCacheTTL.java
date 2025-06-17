package software.board.articleread.cache;

import java.time.Duration;
import lombok.Getter;

@Getter
public class OptimizedCacheTTL {

	private Duration logicalTTL;
	private Duration physicalTTL; // 이 기간 동안에는 redis 에서 데이터를 가져옴 .

	public static final long PHYSICAL_TTL_DELAY_SECONDS = 5;

	public static OptimizedCacheTTL of(long ttlSeconds) {
		OptimizedCacheTTL optimizedCacheTTL = new OptimizedCacheTTL();
		optimizedCacheTTL.logicalTTL = Duration.ofSeconds(ttlSeconds);
		optimizedCacheTTL.physicalTTL =
			optimizedCacheTTL.logicalTTL.plusSeconds(PHYSICAL_TTL_DELAY_SECONDS);
		return optimizedCacheTTL;
	}
}
