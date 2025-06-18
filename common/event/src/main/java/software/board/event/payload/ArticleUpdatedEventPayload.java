package software.board.event.payload;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import software.board.event.EventPayload;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleUpdatedEventPayload extends EventPayload {

	private Long articleId;
	private String title;
	private String content;
	private Long boardId;
	private Long writerId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	private List<String> fileUrls;
}
