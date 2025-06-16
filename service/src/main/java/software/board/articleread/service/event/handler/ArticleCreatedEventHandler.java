package software.board.articleread.service.event.handler;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.board.article.repository.BoardArticleCountRepository;
import software.board.articleread.repository.ArticleIdListRepository;
import software.board.articleread.repository.ArticleQueryModel;
import software.board.articleread.repository.ArticleQueryModelRepository;
import software.board.event.Event;
import software.board.event.EventType;
import software.board.event.payload.ArticleCreatedEventPayload;

@Component
@RequiredArgsConstructor
public class ArticleCreatedEventHandler implements EventHandler<ArticleCreatedEventPayload> {

	private final ArticleQueryModelRepository articleQueryModelRepository;

	private final BoardArticleCountRepository boardArticleCountRepository;
	private final ArticleIdListRepository articleIdListRepository;

	@Override
	public void handle(Event<ArticleCreatedEventPayload> event) {
		ArticleCreatedEventPayload payload = event.getPayload();
		articleQueryModelRepository.create(
			ArticleQueryModel.create(payload),
			Duration.ofDays(3)
		);
	}

	@Override
	public boolean supports(Event<ArticleCreatedEventPayload> event) {
		return event.getType() == EventType.ARTICLE_CREATED;
	}
}
