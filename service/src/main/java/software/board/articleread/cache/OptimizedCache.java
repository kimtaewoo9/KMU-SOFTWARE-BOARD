package software.board.articleread.cache;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;
import software.board.dataserializer.DataSerializer;

@Getter
@ToString
public class OptimizedCache {

	private String data;
	private LocalDateTime expiredAt;

	public static OptimizedCache of(Object data, Duration ttl) {
		OptimizedCache optimizedCache = new OptimizedCache();
		optimizedCache.data = DataSerializer.serialize(data);
		optimizedCache.expiredAt = LocalDateTime.now().plus(ttl);
		return optimizedCache;
	}

	// 자바는 .. get 이나 is 형태의 메서드를 보고 키-값 쌍을 만든다 .
	// 데이터가 아니라 로직임을 @JsonIgnore 를 통해 알려줌 .
	@JsonIgnore
	public boolean isExpired() {
		return LocalDateTime.now().isAfter(expiredAt);
	}

	public <T> T parseData(Class<T> dataType) {
		return DataSerializer.deserialize(data, dataType);
	}
}
