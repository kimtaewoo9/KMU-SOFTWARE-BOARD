package software.board.hotarticle.service.eventhandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.board.event.Event;
import software.board.event.EventType;
import software.board.event.payload.ArticleUnlikedEventPayload;
import software.board.hotarticle.repository.ArticleLikeCountRepository;
import software.board.hotarticle.utils.TimeCalculatorUtils;

@Component
@RequiredArgsConstructor
public class ArticleUnlikedEventHandler implements EventHandler<ArticleUnlikedEventPayload> {

	private final ArticleLikeCountRepository articleLikeCountRepository;

	@Override
	public void handle(Event<ArticleUnlikedEventPayload> event) {
		ArticleUnlikedEventPayload payload = event.getPayload();
		articleLikeCountRepository.createOfUpdate(
			payload.getArticleId(),
			payload.getArticleLikeCount(),
			TimeCalculatorUtils.calculateDurationToMidnight()
		);
	}

	@Override
	public boolean support(Event<ArticleUnlikedEventPayload> event) {
		return event.getType() == EventType.ARTICLE_UNLIKED;
	}

	@Override
	public Long findArticleId(Event<ArticleUnlikedEventPayload> event) {
		return event.getPayload().getArticleId();
	}
}
