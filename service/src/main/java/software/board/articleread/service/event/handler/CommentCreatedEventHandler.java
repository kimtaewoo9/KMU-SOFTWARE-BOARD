package software.board.articleread.service.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.board.articleread.repository.ArticleQueryModelRepository;
import software.board.event.Event;
import software.board.event.EventType;
import software.board.event.payload.CommentCreatedEventPayload;

@Component
@RequiredArgsConstructor
public class CommentCreatedEventHandler implements EventHandler<CommentCreatedEventPayload> {

	private final ArticleQueryModelRepository articleQueryModelRepository;

	@Override
	public void handle(Event<CommentCreatedEventPayload> event) {
		CommentCreatedEventPayload payload = event.getPayload();
		articleQueryModelRepository.read(payload.getArticleId())
			.ifPresent(articleQueryModel -> {
				articleQueryModel.updateBy(payload);
				articleQueryModelRepository.update(articleQueryModel);
			});
	}

	@Override
	public boolean supports(Event<CommentCreatedEventPayload> event) {
		return event.getType() == EventType.COMMENT_CREATED;
	}
}
