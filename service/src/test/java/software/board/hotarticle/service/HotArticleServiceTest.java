package software.board.hotarticle.service;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.board.event.Event;
import software.board.event.EventType;
import software.board.hotarticle.service.eventhandler.EventHandler;

@ExtendWith(MockitoExtension.class)
class HotArticleServiceTest {

	@InjectMocks
	HotArticleService hotArticleService;
	@Mock
	List<EventHandler> eventHandlers;
	@Mock
	HotArticleScoreUpdater hotArticleScoreUpdater;

	@Test
	void handleEventIfEventHandlerNotFoundTest() {
		// given
		Event event = mock(Event.class);
		EventHandler eventHandler = mock(EventHandler.class);

		given(eventHandler.support(event)).willReturn(false);
		given(eventHandlers.stream()).willReturn(Stream.of(eventHandler));

		// when
		hotArticleService.handleEvent(event);

		// then
		verify(event, never()).getType();

		verify(eventHandler, never()).handle(event);
		verify(hotArticleScoreUpdater, never()).update(event, eventHandler);
	}

	@Test
	void handleEventIfArticleCreatedEventTest() {
		// given
		Event event = mock(Event.class);
		EventHandler eventHandler = mock(EventHandler.class);

		given(event.getType()).willReturn(EventType.ARTICLE_CREATED);
		given(eventHandlers.stream()).willReturn(Stream.of(eventHandler));
		given(eventHandler.support(event)).willReturn(true);

		// when
		hotArticleService.handleEvent(event);

		// then
		verify(eventHandler).handle(event);
		verify(hotArticleScoreUpdater, never()).update(event, eventHandler);
	}

	@Test
	void handlerEventIfArticleDeleteEvent() {
		// given
		Event event = mock(Event.class);
		EventHandler eventHandler = mock(EventHandler.class);

		given(event.getType()).willReturn(EventType.ARTICLE_DELETED);
		given(eventHandler.support(event)).willReturn(true);
		given(eventHandlers.stream()).willReturn(Stream.of(eventHandler));

		// when
		hotArticleService.handleEvent(event);

		// then
		verify(eventHandler).handle(event);
		verify(hotArticleScoreUpdater, never()).update(event, eventHandler);
	}

	@Test
	void shouldUpdateScoreWhenEventIsReceived() {
		Event event = mock(Event.class);
		EventHandler eventHandler = mock(EventHandler.class);

		given(event.getType()).willReturn(EventType.ARTICLE_LIKED);
		given(eventHandler.support(event)).willReturn(true);
		given(eventHandlers.stream()).willReturn(Stream.of(eventHandler));

		// when
		hotArticleService.handleEvent(event);

		// then
		verify(hotArticleScoreUpdater).update(event, eventHandler);
		verify(eventHandler, never()).handle(event);
	}
}
