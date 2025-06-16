package software.board.articleread.service.event.handler;

import software.board.event.Event;
import software.board.event.EventPayload;

public interface EventHandler<T extends EventPayload> {

	void handle(Event<T> event);

	boolean supports(Event<T> event);
}
