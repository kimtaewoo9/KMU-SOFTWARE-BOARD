package software.board.common.outboxmessagerelay;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageRelay {

	private final OutboxRepository outboxRepository;
	private final MessageRelayCoordinator messageRelayCoordinator;
	private final KafkaTemplate<String, String> messageRelayKafkaTemplate;

	@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
	public void createOutbox(OutboxEvent outboxEvent) {
		outboxRepository.save(outboxEvent.getOutbox());
	}

	@Async("messageRelayPublishEventExecutor")
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void publishEvent(OutboxEvent outboxEvent) {
		publishEvent(outboxEvent.getOutbox());
	}

	private void publishEvent(Outbox outbox) {
		try {
			messageRelayKafkaTemplate.send(
				outbox.getEventType().getTopic(),
				String.valueOf(outbox.getShardKey()),
				outbox.getPayload()
			).get(1, TimeUnit.SECONDS); // send 를 비동기 -> 동기로 바꿈 .
			outboxRepository.delete(outbox); // 처리된 outbox 를 삭제해줌 .
		} catch (Exception e) {
			log.error("[MessageRelay.publishEvent] outbox={}", outbox, e);
		}
	}

	@Scheduled(
		fixedDelay = 10,
		initialDelay = 5,
		timeUnit = TimeUnit.SECONDS,
		scheduler = "messageRelayPublishPendingEventExecutor"
	)
	public void publishPendingEvent() {
		AssignedShard assignedShard = messageRelayCoordinator.assignShards();
		log.info("[MessageRelay.publishPendingEvent] assignedShard size={}",
			assignedShard.getShards().size());
		for (Long shard : assignedShard.getShards()) {
			List<Outbox> outboxes = outboxRepository.findAllByShardKeyAndCreatedAtLessThanEqualOrderByCreatedAtAsc(
				shard,
				LocalDateTime.now().minusSeconds(10), // 생성된지 10초가 지난 이벤트
				Pageable.ofSize(100)
			);
			for (Outbox outbox : outboxes) {
				publishEvent(outbox);
			}
		}
	}
}
