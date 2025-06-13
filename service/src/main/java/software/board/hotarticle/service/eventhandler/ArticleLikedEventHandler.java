package software.board.hotarticle.service.eventhandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.board.event.Event;
import software.board.event.EventType;
import software.board.event.payload.ArticleLikedEventPayload;
import software.board.hotarticle.repository.ArticleLikeCountRepository;
import software.board.hotarticle.utils.TimeCalculatorUtils;

@Component
@RequiredArgsConstructor
public class ArticleLikedEventHandler implements EventHandler<ArticleLikedEventPayload> {

	private final ArticleLikeCountRepository articleLikeCountRepository;

	@Override
	public void handle(Event<ArticleLikedEventPayload> event) {
		ArticleLikedEventPayload payload = event.getPayload();
		articleLikeCountRepository.createOfUpdate(
			payload.getArticleId(),
			payload.getArticleLikeCount(),
			TimeCalculatorUtils.calculateDurationToMidnight()
		);
	}

	@Override
	public boolean support(Event<ArticleLikedEventPayload> event) {
		return event.getType() == EventType.ARTICLE_LIKED;
	}

	@Override
	public Long findArticleId(Event<ArticleLikedEventPayload> event) {
		return event.getPayload().getArticleId();
	}
}
