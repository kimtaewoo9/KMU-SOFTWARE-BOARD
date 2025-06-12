package software.board.event.payload;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import software.board.event.EventPayload;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdatedEventPayload extends EventPayload {

	private Long commentId;
	private String content;
	private Long articleId;
	private Long writerId;
	private Boolean deleted;
	private LocalDateTime createdAt;
}
