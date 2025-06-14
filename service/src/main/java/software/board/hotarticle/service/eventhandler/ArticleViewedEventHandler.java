package software.board.hotarticle.service.eventhandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.board.event.Event;
import software.board.event.EventType;
import software.board.event.payload.ArticleViewedEventPayload;

@Component
@RequiredArgsConstructor
public class ArticleViewedEventHandler implements EventHandler<ArticleViewedEventPayload> {

	@Override
	public void handle(Event<ArticleViewedEventPayload> event) {
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
