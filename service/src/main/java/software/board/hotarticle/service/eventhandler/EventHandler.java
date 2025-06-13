package software.board.hotarticle.service.eventhandler;

import software.board.event.Event;
import software.board.event.EventPayload;

public interface EventHandler<T extends EventPayload> {

	void handle(Event<T> event);

	boolean support(Event<T> event);

	Long findArticleId(Event<T> event);
}
