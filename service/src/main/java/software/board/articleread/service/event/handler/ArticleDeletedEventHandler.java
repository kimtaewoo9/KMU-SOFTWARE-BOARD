package software.board.articleread.service.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.board.articleread.repository.ArticleIdListRepository;
import software.board.articleread.repository.ArticleQueryModelRepository;
import software.board.event.Event;
import software.board.event.EventType;
import software.board.event.payload.ArticleDeletedEventPayload;

@Component
@RequiredArgsConstructor
public class ArticleDeletedEventHandler implements EventHandler<ArticleDeletedEventPayload> {

	private final ArticleQueryModelRepository articleQueryModelRepository;
	private final ArticleIdListRepository articleIdListRepository;

	@Override
	public void handle(Event<ArticleDeletedEventPayload> event) {
		ArticleDeletedEventPayload payload = event.getPayload();
		articleQueryModelRepository.delete(payload.getArticleId());
		articleIdListRepository.delete(payload.getBoardId(), payload.getArticleId());
	}

	@Override
	public boolean supports(Event<ArticleDeletedEventPayload> event) {
		return event.getType() == EventType.ARTICLE_DELETED;
	}
}
