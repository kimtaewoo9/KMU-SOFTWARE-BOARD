package software.board.articleread.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import software.board.articleread.service.ArticleReadService;
import software.board.event.Event;
import software.board.event.EventPayload;
import software.board.event.EventType.Topic;

@Component
@RequiredArgsConstructor
@Slf4j
public class ArticleReadEventConsumer {

	private final ArticleReadService articleReadService;

	@KafkaListener(topics = {
		Topic.SOFTWARE_BOARD_ARTICLE,
		Topic.SOFTWARE_BOARD_COMMENT,
		Topic.SOFTWARE_BOARD_LIKE
	})
	public void listen(String message, Acknowledgment ack) {
		log.info("[ArticleReadEventConsumer.listen] message={}", message);
		Event<EventPayload> event = Event.fromJson(message);
		if (event != null) {
			articleReadService.handleEvent(event);
		}
		ack.acknowledge();
	}
}
