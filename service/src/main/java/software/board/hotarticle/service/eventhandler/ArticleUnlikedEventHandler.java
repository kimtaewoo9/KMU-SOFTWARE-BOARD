package software.board.hotarticle.service.eventhandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.board.event.Event;
import software.board.event.EventType;
import software.board.event.payload.ArticleUnlikedEventPayload;

@Component
@RequiredArgsConstructor
public class ArticleUnlikedEventHandler implements EventHandler<ArticleUnlikedEventPayload> {

	@Override
	public void handle(Event<ArticleUnlikedEventPayload> event) {
	}

	@Override
	public boolean support(Event<ArticleUnlikedEventPayload> event) {
		return event.getType() == EventType.ARTICLE_UNLIKED;
	}

	@Override
	public Long findArticleId(Event<ArticleUnlikedEventPayload> event) {
		return event.getPayload().getArticleId();
	}
}
