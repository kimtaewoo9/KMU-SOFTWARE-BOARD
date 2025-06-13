package software.board.hotarticle.service.eventhandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.board.event.Event;
import software.board.event.EventType;
import software.board.event.payload.ArticleCreatedEventPayload;
import software.board.hotarticle.repository.ArticleCreatedTimeRepository;
import software.board.hotarticle.utils.TimeCalculatorUtils;

@Component
@RequiredArgsConstructor
public class ArticleCreatedEventHandler implements EventHandler<ArticleCreatedEventPayload> {
	
	private final ArticleCreatedTimeRepository articleCreatedTimeRepository;

	@Override
	public void handle(Event<ArticleCreatedEventPayload> event) {
		ArticleCreatedEventPayload payload = event.getPayload();
		articleCreatedTimeRepository.createOrUpdate(
			payload.getArticleId(),
			payload.getCreatedAt(),
			TimeCalculatorUtils.calculateDurationToMidnight() // 하루만 저장 .
		);
	}

	@Override
	public boolean support(Event<ArticleCreatedEventPayload> event) {
		return event.getType() == EventType.ARTICLE_CREATED;
	}

	@Override
	public Long findArticleId(Event<ArticleCreatedEventPayload> event) {
		return event.getPayload().getArticleId();
	}
}
