package software.board.event;

import lombok.Getter;
import software.board.dataserializer.DataSerializer;

@Getter
public class Event<T extends EventPayload> {

	private Long eventId;
	private EventType type;
	private T payload;

	public static Event<EventPayload> of(Long eventId, EventType type, EventPayload payload) {
		Event<EventPayload> event = new Event<>();
		event.eventId = eventId;
		event.type = type;
		event.payload = payload;

		return event;
	}

	// 이벤트 객체를 json으로 변환
	public String toJson() {
		return DataSerializer.serialize(this);
	}

	public static Event<EventPayload> fromJson(String json) {

		// event 가 Generic 타입이기 때문에 .. eventRaw 로 받은 다음에 그걸 event 객체로 바꿈 .
		EventRaw eventRaw = DataSerializer.deserialize(json, EventRaw.class);
		if (eventRaw == null) {
			return null;
		}
		Event<EventPayload> event = new Event<>();
		event.eventId = eventRaw.getEventId();
		event.type = EventType.from(eventRaw.getType());
		event.payload = DataSerializer.deserialize(
			eventRaw.getPayload(),
			event.type.getPayloadClass());

		return event;
	}

	@Getter
	private static class EventRaw {

		private Long eventId;
		private String type;
		private Object payload; // payload 를 그냥 Object 타입으로 받음 .
	}
}
