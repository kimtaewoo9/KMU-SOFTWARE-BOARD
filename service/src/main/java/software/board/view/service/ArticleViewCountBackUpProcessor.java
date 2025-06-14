package software.board.view.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import software.board.common.outboxmessagerelay.OutboxEventPublisher;
import software.board.event.EventType;
import software.board.event.payload.ArticleViewedEventPayload;
import software.board.view.entity.ArticleViewCount;
import software.board.view.repository.ArticleViewCountBackUpRepository;

@Component
@RequiredArgsConstructor
public class ArticleViewCountBackUpProcessor {

	private final ArticleViewCountBackUpRepository articleViewCountBackUpRepository;

	private final OutboxEventPublisher outboxEventPublisher;

	@Transactional
	public void backUp(Long articleId, Long viewCount) {
		int result = articleViewCountBackUpRepository.updateViewCount(articleId, viewCount);
		if (result == 0) {
			articleViewCountBackUpRepository.findById(articleId)
				.ifPresentOrElse(ignored -> {
					}, // 레코드가 있지만, DB의 조회수가 더 높아서 result = 0이 나온 경우 ignore
					() -> articleViewCountBackUpRepository.save(
						ArticleViewCount.init(articleId, viewCount)
					)
				);
		}

		outboxEventPublisher.publish(
			EventType.ARTICLE_VIEWED,
			ArticleViewedEventPayload.builder()
				.articleId(articleId)
				.articleViewCount(viewCount)
				.build(),
			articleId
		);
	}
}
