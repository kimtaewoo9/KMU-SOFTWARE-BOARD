package software.board.hotarticle.service.eventhandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.board.event.Event;
import software.board.event.EventType;
import software.board.event.payload.ArticleDeletedEventPayload;
import software.board.hotarticle.repository.HotArticleListRepository;

@Component
@RequiredArgsConstructor
public class HotArticleListSyncHandler implements EventHandler<ArticleDeletedEventPayload> {

	private final HotArticleListRepository hotArticleListRepository;

	@Override
	public void handle(Event<ArticleDeletedEventPayload> event) {
		ArticleDeletedEventPayload payload = event.getPayload();
		hotArticleListRepository.remove(payload.getCreatedAt(), payload.getArticleId());
	}

	@Override
	public boolean support(Event<ArticleDeletedEventPayload> event) {
		return event.getType() == EventType.ARTICLE_DELETED;
	}

	@Override
	public Long findArticleId(Event<ArticleDeletedEventPayload> event) {
		return event.getPayload().getArticleId();
	}
}
