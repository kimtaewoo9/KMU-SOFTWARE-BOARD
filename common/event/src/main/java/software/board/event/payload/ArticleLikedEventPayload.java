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
public class ArticleLikedEventPayload extends EventPayload {

	private Long articleLikeId;
	private Long articleId;
	private Long userId;
	private LocalDateTime createdAt;
	private Long articleLikeCount;
}
