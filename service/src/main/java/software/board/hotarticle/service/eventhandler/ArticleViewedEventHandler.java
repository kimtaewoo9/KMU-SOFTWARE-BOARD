package software.board.hotarticle.service.eventhandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.board.event.Event;
import software.board.event.EventType;
import software.board.event.payload.ArticleViewedEventPayload;
import software.board.hotarticle.repository.HotArticleViewCountRepository;
import software.board.hotarticle.utils.TimeCalculatorUtils;

@Component
@RequiredArgsConstructor
public class ArticleViewedEventHandler implements EventHandler<ArticleViewedEventPayload> {

	private final HotArticleViewCountRepository hotArticleViewCountRepository;

	@Override
	public void handle(Event<ArticleViewedEventPayload> event) {
		ArticleViewedEventPayload payload = event.getPayload();
		hotArticleViewCountRepository.createOrUpdate(
			payload.getArticleId(),
			payload.getArticleViewCount(),
			TimeCalculatorUtils.calculateDurationToMidnight()
		);
	}

	@Override
	public boolean support(Event<ArticleViewedEventPayload> event) {
		return event.getType() == EventType.ARTICLE_VIEWED;
	}

	@Override
	public Long findArticleId(Event<ArticleViewedEventPayload> event) {
		return event.getPayload().getArticleId();
	}
}
