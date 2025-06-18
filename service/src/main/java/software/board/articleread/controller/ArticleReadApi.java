package software.board.articleread.controller; // 패키지 경로는 실제 프로젝트에 맞게 조정하세요.

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import software.board.articleread.service.response.ArticleReadPageResponse;
import software.board.articleread.service.response.ArticleReadResponse;

@Tag(name = "게시글 조회 관리", description = "게시글 조회 관련 API (조회수, 좋아요, 댓글 수 포함)")
public interface ArticleReadApi {

	@Operation(summary = "게시글 상세 조회", description = "게시글 ID를 이용해 댓글, 좋아요, 조회수 등을 포함한 상세 정보를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공",
			content = @Content(schema = @Schema(implementation = ArticleReadResponse.class))),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 게시글"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	ArticleReadResponse read(
		@Parameter(description = "조회할 게시글의 ID", required = true, example = "1")
		@PathVariable("articleId") Long articleId
	);

	@Operation(summary = "게시글 목록 페이지네이션 조회", description = "페이지 번호와 사이즈를 이용해 게시글 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공",
			content = @Content(schema = @Schema(implementation = ArticleReadPageResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 (페이지 번호 또는 사이즈 오류)")
	})
	ArticleReadPageResponse readAll(
		@Parameter(description = "게시판 ID", required = true, example = "1") @RequestParam("boardId") Long boardId,
		@Parameter(description = "페이지 번호 (1부터 시작)", required = true, example = "1") @RequestParam("page") Long page,
		@Parameter(description = "한 페이지에 보여줄 게시글 수", required = true, example = "10") @RequestParam("pageSize") Long pageSize
	);

	@Operation(summary = "게시글 목록 무한 스크롤 조회", description = "커서(마지막 게시글 ID)를 이용해 다음 게시글 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공",
			content = @Content(schema = @Schema(type = "array", implementation = ArticleReadResponse.class)))
	})
	List<ArticleReadResponse> readAllInfiniteScroll(
		@Parameter(description = "게시판 ID", required = true, example = "1") @RequestParam("boardId") Long boardId,
		@Parameter(description = "이전 페이지의 마지막 게시글 ID (첫 페이지는 생략)") @RequestParam(value = "lastArticleId", required = false) Long lastArticleId,
		@Parameter(description = "한 번에 가져올 게시글 수", required = true, example = "10") @RequestParam("pageSize") Long pageSize
	);
}
