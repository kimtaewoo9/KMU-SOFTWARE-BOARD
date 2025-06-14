package software.board.hotarticle.service.eventhandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.board.event.Event;
import software.board.event.EventType;
import software.board.event.payload.ArticleCreatedEventPayload;

@Component
@RequiredArgsConstructor
public class ArticleCreatedEventHandler implements EventHandler<ArticleCreatedEventPayload> {

	@Override
	public void handle(Event<ArticleCreatedEventPayload> event) {
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
