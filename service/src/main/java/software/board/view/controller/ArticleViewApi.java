package software.board.view.controller; // 패키지 경로는 실제 프로젝트에 맞게 조정하세요.

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "조회수 관리", description = "게시글 조회수 관련 API")
public interface ArticleViewApi {

	@Operation(summary = "게시글 조회수 조회", description = "특정 게시글의 현재 조회수를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공",
			content = @Content(schema = @Schema(implementation = Long.class))),
		@ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
	})
	Long count(
		@Parameter(description = "조회수를 확인할 게시글의 ID", required = true, example = "1")
		@PathVariable("articleId") Long articleId
	);

	@Operation(summary = "게시글 조회수 증가", description = "특정 게시글의 조회수를 1 증가시키고, 증가된 후의 총 조회수를 반환합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회수 증가 성공",
			content = @Content(schema = @Schema(implementation = Long.class))),
		@ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
	})
	Long increase(
		@Parameter(description = "조회수를 증가시킬 게시글의 ID", required = true, example = "1")
		@PathVariable("articleId") Long articleId
	);
}
