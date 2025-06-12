package software.board.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.board.event.payload.ArticleCreatedEventPayload;
import software.board.event.payload.ArticleDeletedEventPayload;
import software.board.event.payload.ArticleLikedEventPayload;
import software.board.event.payload.ArticleUpdatedEventPayload;
import software.board.event.payload.ArticleViewedEventPayload;
import software.board.event.payload.CommentCreatedEventPayload;
import software.board.event.payload.CommentDeletedEventPayload;
import software.board.event.payload.CommentUpdatedEventPayload;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum EventType {

	ARTICLE_CREATED(ArticleCreatedEventPayload.class, Topic.SOFTWARE_BOARD_ARTICLE),
	ARTICLE_UPDATED(ArticleUpdatedEventPayload.class, Topic.SOFTWARE_BOARD_ARTICLE),
	ARTICLE_DELETED(ArticleDeletedEventPayload.class, Topic.SOFTWARE_BOARD_ARTICLE),
	COMMENT_CREATED(CommentCreatedEventPayload.class, Topic.SOFTWARE_BOARD_COMMENT),
	COMMENT_UPDATED(CommentUpdatedEventPayload.class, Topic.SOFTWARE_BOARD_COMMENT),
	COMMENT_DELETED(CommentDeletedEventPayload.class, Topic.SOFTWARE_BOARD_COMMENT),
	ARTICLE_LIKED(ArticleLikedEventPayload.class, Topic.SOFTWARE_BOARD_LIKE),
	ARTICLE_UNLIKED(ArticleLikedEventPayload.class, Topic.SOFTWARE_BOARD_LIKE),
	ARTICLE_VIEWED(ArticleViewedEventPayload.class, Topic.SOFTWARE_BOARD_VIEW);

	private final Class<? extends EventPayload> payloadClass; // 이벤트가 어떤 pay load type인가
	private final String topic; // 어떤 카프카 토픽으로 전달되는지

	public static EventType from(String type) {
		try {
			return valueOf(type);
		} catch (Exception e) {
			log.error("[EventType.from] type={}", type, e);
			return null;
		}
	}

	public static class Topic {

		public static final String SOFTWARE_BOARD_ARTICLE = "software-board-article";
		public static final String SOFTWARE_BOARD_COMMENT = "software-board-comment";
		public static final String SOFTWARE_BOARD_LIKE = "software-board-like";
		public static final String SOFTWARE_BOARD_VIEW = "software-board-view";
	}
}
