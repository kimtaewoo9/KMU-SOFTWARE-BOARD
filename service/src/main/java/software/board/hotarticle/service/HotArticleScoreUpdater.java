package software.board.hotarticle.service;

import jakarta.persistence.EntityNotFoundException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.board.article.repository.ArticleRepository;
import software.board.event.Event;
import software.board.event.EventPayload;
import software.board.hotarticle.repository.HotArticleListRepository;
import software.board.hotarticle.service.eventhandler.EventHandler;

@Component
@RequiredArgsConstructor
public class HotArticleScoreUpdater {

	private final HotArticleListRepository hotArticleListRepository;
	private final HotArticleScoreCalculator hotArticleScoreCalculator;
	private final ArticleRepository articleRepository;

	private static final long HOT_ARTICLE_COUNT = 10;
	private static final Duration HOT_ARTICLE_TTL = Duration.ofDays(10);

	public void update(Event<EventPayload> event, EventHandler<EventPayload> eventHandler) {
		Long articleId = eventHandler.findArticleId(event);
		LocalDateTime createdTime = articleRepository.findCreatedAtByArticleId(articleId)
			.orElseThrow(
				() -> new EntityNotFoundException(
					"[HotArticleScoreUpdater.update] article not found.")
			);

		if (!isArticleCreatedToday(createdTime)) {
			return;
		}

		// 오늘 날짜라면 .
		eventHandler.handle(event);

		long score = hotArticleScoreCalculator.calculate(articleId);
		hotArticleListRepository.add(
			articleId,
			createdTime,
			score,
			HOT_ARTICLE_COUNT,
			HOT_ARTICLE_TTL
		);
	}

	private boolean isArticleCreatedToday(LocalDateTime createdTime) {
		return createdTime != null && createdTime.toLocalDate().equals(LocalDate.now());
	}
}
