package software.board.common.outboxmessagerelay;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import software.board.common.snowflake.Snowflake;
import software.board.event.Event;
import software.board.event.EventPayload;
import software.board.event.EventType;

@Component
@RequiredArgsConstructor
public class OutboxEventPublisher {

	private final Snowflake outboxIdSnowflake = new Snowflake();
	private final Snowflake eventIdSnowflake = new Snowflake();
	private final ApplicationEventPublisher applicationEventPublisher;

	// 각각의 서비스에서 outbox event publisher 를 통해 이벤트를 발행함 .
	public void publish(EventType type, EventPayload payload, Long shardKey) {
		Outbox outbox = Outbox.create(
			outboxIdSnowflake.nextId(),
			type,
			Event.of(eventIdSnowflake.nextId(), type, payload).toJson(),
			shardKey % MessageRelayConstants.SHARD_COUNT
		);

		// outbox 객체를 outboxEvent 객체에 담아서 전송함 .
		applicationEventPublisher.publishEvent(OutboxEvent.of(outbox));
	}
}
