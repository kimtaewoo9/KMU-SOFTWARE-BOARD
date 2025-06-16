package software.board.articleread.service.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.board.articleread.repository.ArticleQueryModelRepository;
import software.board.event.Event;
import software.board.event.EventType;
import software.board.event.payload.ArticleUnlikedEventPayload;

@Component
@RequiredArgsConstructor
public class ArticleUnlikedEventHandler implements EventHandler<ArticleUnlikedEventPayload> {

	private final ArticleQueryModelRepository articleQueryModelRepository;

	@Override
	public void handle(Event<ArticleUnlikedEventPayload> event) {
		ArticleUnlikedEventPayload payload = event.getPayload();
		articleQueryModelRepository.read(payload.getArticleId())
			.ifPresent(articleQueryModel -> {
				articleQueryModel.updateBy(payload);
				articleQueryModelRepository.update(articleQueryModel);
			});
	}

	@Override
	public boolean supports(Event<ArticleUnlikedEventPayload> event) {
		return event.getType() == EventType.ARTICLE_UNLIKED;
	}
}
