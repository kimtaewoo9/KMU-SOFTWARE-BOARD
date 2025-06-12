package software.board.dataserializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class DataSerializer {

	private static final ObjectMapper objectMapper = initialize();

	private static ObjectMapper initialize() {
		return new ObjectMapper()
			.registerModule(new JavaTimeModule())
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	// 문자열을 특정 자바 클래스의 객체로 변환 .
	public static <T> T deserialize(String data, Class<T> clazz) {
		try {
			return objectMapper.readValue(data, clazz);
		} catch (JsonProcessingException e) {
			log.error("[DataSerializer.deserialize] data={}, class={}", data, clazz);
			return null;
		}
	}

	// 자바 객체를 특정 자바 클래스의 객체로 변환 . redis 에서 받아서 객체로 바꿔서 써야하니까.
	public static <T> T deserialize(Object data, Class<T> clazz) {
		return objectMapper.convertValue(data, clazz);
	}

	// 객체를 . 문자열로
	public static String serialize(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			log.error("[DataSerializer.serialize] object={}", object);
			return null;
		}
	}
}
