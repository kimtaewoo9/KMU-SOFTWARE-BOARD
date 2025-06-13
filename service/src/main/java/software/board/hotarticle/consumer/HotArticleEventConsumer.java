package software.board.hotarticle.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import software.board.event.Event;
import software.board.event.EventPayload;
import software.board.event.EventType.Topic;
import software.board.hotarticle.service.HotArticleService;

@Component
@RequiredArgsConstructor
@Slf4j
public class HotArticleEventConsumer {

	private final HotArticleService hotArticleService;

	@KafkaListener(topics = {
		Topic.SOFTWARE_BOARD_ARTICLE,
		Topic.SOFTWARE_BOARD_COMMENT,
		Topic.SOFTWARE_BOARD_LIKE,
		Topic.SOFTWARE_BOARD_VIEW
	})
	public void listen(String message, Acknowledgment ack) {
		log.info("[HotArticleEventConsumer.listen] received message={}", message);
		Event<EventPayload> event = Event.fromJson(message);
		if (event != null) {
			hotArticleService.handleEvent(event);
		}
		ack.acknowledge();
	}
}
