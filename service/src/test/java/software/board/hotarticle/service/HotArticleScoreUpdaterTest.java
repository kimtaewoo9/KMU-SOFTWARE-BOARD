package software.board.hotarticle.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.board.event.Event;
import software.board.hotarticle.repository.ArticleCreatedTimeRepository;
import software.board.hotarticle.repository.HotArticleListRepository;
import software.board.hotarticle.service.eventhandler.EventHandler;

@ExtendWith(MockitoExtension.class)
class HotArticleScoreUpdaterTest {

	@InjectMocks
	HotArticleScoreUpdater hotArticleScoreUpdater;
	@Mock
	HotArticleListRepository hotArticleListRepository;
	@Mock
	HotArticleScoreCalculator hotArticleScoreCalculator;
	@Mock
	ArticleCreatedTimeRepository articleCreatedTimeRepository;

	@Test
	void updateIfArticleNotCreatedTodayTest() {
		// given
		Long articleId = 1L;
		Event event = mock(Event.class);
		EventHandler eventHandler = mock(EventHandler.class);

		given(eventHandler.findArticleId(event)).willReturn(articleId);

		LocalDateTime createdTime = LocalDateTime.now().minusDays(1);
		given(articleCreatedTimeRepository.read(articleId)).willReturn(createdTime);

		// when
		hotArticleScoreUpdater.update(event, eventHandler);

		// then
		verify(eventHandler, never()).handle(event);
		verify(hotArticleScoreCalculator, never()).calculate(articleId);
	}

	@Test
	void updateTest() {
		// given
		// event, event handler 를 받아서 eventHandler 로 event 에 있는 articleId 찾음.
		// 그냥 event 에서
		Long articleId = 1L;
		Event event = mock(Event.class);

		EventHandler eventHandler = mock(EventHandler.class);

		given(eventHandler.findArticleId(event)).willReturn(articleId);

		LocalDateTime createdTime = LocalDateTime.now();
		given(articleCreatedTimeRepository.read(articleId)).willReturn(createdTime);

		// when
		hotArticleScoreUpdater.update(event, eventHandler);

		// then
		verify(eventHandler).handle(event);
		verify(hotArticleScoreCalculator).calculate(articleId);
		verify(hotArticleListRepository).add(
			anyLong(), any(LocalDateTime.class), anyLong(), anyLong(), any(Duration.class)
		);
	}
}
