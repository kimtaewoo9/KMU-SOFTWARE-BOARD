package software.board.hotarticle.service;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.board.event.Event;
import software.board.event.EventPayload;
import software.board.event.EventType;
import software.board.hotarticle.client.ArticleClient;
import software.board.hotarticle.repository.HotArticleListRepository;
import software.board.hotarticle.service.eventhandler.EventHandler;
import software.board.hotarticle.service.response.HotArticleResponse;

@Service
@RequiredArgsConstructor
public class HotArticleService {

	private final ArticleClient articleClient;
	private final List<EventHandler> eventHandlers;
	private final HotArticleScoreUpdater hotArticleScoreUpdater;
	private final HotArticleListRepository hotArticleListRepository;

	@Transactional
	public void handleEvent(Event<EventPayload> event) {
		EventHandler<EventPayload> eventHandler = findEventHandler(event);
		if (eventHandler == null) {
			return;
		}

		if (!isArticleLikedCreatedOrDeleted(event)) {
			eventHandler.handle(event);
		} else {
			// 좋아요에 대한 이벤트라면 score 를 update 함
			hotArticleScoreUpdater.update(event, eventHandler);
		}
	}

	private boolean isArticleLikedCreatedOrDeleted(Event<EventPayload> event) {
		return event.getType() == EventType.ARTICLE_LIKED ||
			event.getType() == EventType.ARTICLE_UNLIKED;
	}

	private EventHandler<EventPayload> findEventHandler(Event<EventPayload> event) {
		return eventHandlers.stream()
			.filter(eventHandler -> eventHandler.support(event))
			.findFirst()
			.orElse(null);
	}

	// 1. articleId 로 article client 에서 article response 가져옴 2. hot article response 로 바꿈
	public List<HotArticleResponse> getHotArticleList(String dateStr) {
		return hotArticleListRepository.readAll(dateStr).stream()
			.map(articleClient::read)
			.filter(Objects::nonNull)
			.map(HotArticleResponse::from)
			.toList();
	}
}
