package software.board.common.outboxmessagerelay;

import lombok.Getter;
import lombok.ToString;

// DB에 저장된 outbox 를 전달할 outbox event 객체
@Getter
@ToString
public class OutboxEvent {

	// 다른 정보를 더 담아도 됨 .traceId 나 발행 시간 이런거 .
	private Outbox outbox;

	public static OutboxEvent of(Outbox outbox) {
		OutboxEvent outboxEvent = new OutboxEvent();
		outboxEvent.outbox = outbox;

		return outboxEvent;
	}
}
