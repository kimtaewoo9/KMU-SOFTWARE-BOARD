package software.board.event;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import software.board.event.payload.ArticleCreatedEventPayload;
import software.board.event.payload.ArticleDeletedEventPayload;
import software.board.event.payload.ArticleUpdatedEventPayload;

class EventTest {

	// 직렬화, 역직렬화 test
	@Test
	void serde() {
		// given
		ArticleCreatedEventPayload payload = ArticleCreatedEventPayload.builder()
			.articleId(1L)
			.title("title")
			.content("content")
			.boardId(1L)
			.writerId(1L)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.boardArticleCount(22L)
			.build();

		Event<EventPayload> event = Event.of(
			1234L,
			EventType.ARTICLE_CREATED,
			payload
		);

		// event 객체를 String 으로 만듦 ..
		String json = event.toJson();
		System.out.println("json: " + json);

		// when
		Event<EventPayload> result = Event.fromJson(json);

		// then
		// ArticleCreatedEventPayload !
		assertThat(result.getEventId()).isEqualTo(event.getEventId());
		assertThat(result.getType()).isEqualTo(event.getType());
		assertThat(result.getPayload()).isInstanceOf(payload.getClass());

		ArticleCreatedEventPayload resultPayload = (ArticleCreatedEventPayload) result.getPayload();

		assertThat(resultPayload.getArticleId()).isEqualTo(payload.getArticleId());
	}

	@Test
	void serdeTestArticleDeletedEventPayload() {
		ArticleDeletedEventPayload payload = ArticleDeletedEventPayload.builder()
			.articleId(1L)
			.title("title")
			.content("content")
			.boardId(1L)
			.writerId(1L)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();

		Event<EventPayload> event = Event.of(
			1234L,
			EventType.ARTICLE_DELETED,
			payload
		);

		String json = event.toJson();
		System.out.println("json = " + json);

		// when
		Event<EventPayload> result = Event.fromJson(json);

		// then
		assertThat(result.getEventId()).isEqualTo(event.getEventId());
		assertThat(result.getType()).isEqualTo(event.getType());
		assertThat(result.getPayload()).isInstanceOf(payload.getClass());

		ArticleDeletedEventPayload resultPayload = (ArticleDeletedEventPayload) result.getPayload();

		assertThat(resultPayload.getArticleId()).isEqualTo(payload.getArticleId());
	}

	@Test
	void serdeTestArticleUpdatePayload() {
		ArticleUpdatedEventPayload payload = ArticleUpdatedEventPayload.builder()
			.articleId(1L)
			.title("title")
			.content("content")
			.boardId(1L)
			.writerId(1L)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();

		Event<EventPayload> event = Event.of(
			1L,
			EventType.ARTICLE_UPDATED,
			payload
		);

		String json = event.toJson();
		System.out.println("json = " + json);

		// when (json -> event 객체)
		Event<EventPayload> result = Event.fromJson(json);

		// then
		assertThat(result.getEventId()).isEqualTo(event.getEventId());
		assertThat(result.getPayload()).isInstanceOf(event.getPayload().getClass());

		ArticleUpdatedEventPayload resultPayload = (ArticleUpdatedEventPayload) result.getPayload();
		assertThat(payload.getArticleId()).isEqualTo(resultPayload.getArticleId());
	}
}
