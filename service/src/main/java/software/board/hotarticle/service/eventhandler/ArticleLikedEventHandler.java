package software.board.hotarticle.service.eventhandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.board.event.Event;
import software.board.event.EventType;
import software.board.event.payload.ArticleLikedEventPayload;

@Component
@RequiredArgsConstructor
public class ArticleLikedEventHandler implements EventHandler<ArticleLikedEventPayload> {

	@Override
	public void handle(Event<ArticleLikedEventPayload> event) {
	}

	@Override
	public boolean support(Event<ArticleLikedEventPayload> event) {
		return event.getType() == EventType.ARTICLE_LIKED;
	}

	@Override
	public Long findArticleId(Event<ArticleLikedEventPayload> event) {
		return event.getPayload().getArticleId();
	}
}
