package software.board.common.outboxmessagerelay;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import software.board.event.EventType;

@Table(name = "outbox")
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Outbox {

	@Id
	private Long outboxId;
	@Enumerated(EnumType.STRING)
	private EventType eventType;
	private String payload; // event 객체를 통째로 JSON 문자열로 바꿔서 저장함 .
	private Long shardKey;
	private LocalDateTime createdAt;

	public static Outbox create(Long outboxId, EventType eventType, String payload, Long shardKey) {
		Outbox outbox = new Outbox();
		outbox.outboxId = outboxId;
		outbox.eventType = eventType;
		outbox.payload = payload;
		outbox.shardKey = shardKey;
		outbox.createdAt = LocalDateTime.now();
		return outbox;
	}
}
