package software.board.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import software.board.comment.service.request.CommentCreateRequest;
import software.board.comment.service.request.CommentUpdateRequest;
import software.board.comment.service.response.CommentPageResponse;
import software.board.comment.service.response.CommentResponse;

@Tag(name = "댓글 관리", description = "댓글 관련 API")
public interface CommentApi {

	@Operation(summary = "댓글 단건 조회", description = "특정 댓글의 상세 정보를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공",
			content = @Content(schema = @Schema(implementation = CommentResponse.class))),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 댓글")
	})
	CommentResponse read(
		@Parameter(description = "조회할 댓글의 ID", required = true, example = "1")
		@PathVariable("commentId") Long commentId
	);

	@Operation(summary = "댓글 목록 페이지네이션 조회", description = "페이지 번호와 사이즈를 이용해 댓글 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공",
			content = @Content(schema = @Schema(implementation = CommentPageResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청")
	})
	CommentPageResponse readAll(
		@Parameter(description = "게시글 ID", required = true, example = "1") @RequestParam("articleId") Long articleId,
		@Parameter(description = "페이지 번호 (1부터 시작)", required = true, example = "1") @RequestParam("page") Long page,
		@Parameter(description = "한 페이지에 보여줄 댓글 수", required = true, example = "10") @RequestParam("pageSize") Long pageSize
	);

	@Operation(summary = "댓글 목록 무한 스크롤 조회", description = "커서(마지막 댓글 ID 등)를 이용해 다음 댓글 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공",
			content = @Content(schema = @Schema(type = "array", implementation = CommentResponse.class)))
	})
	List<CommentResponse> readAllInfiniteScroll(
		@Parameter(description = "게시글 ID", required = true, example = "1") @RequestParam("articleId") Long articleId,
		@Parameter(description = "이전 페이지의 마지막 부모 댓글 ID") @RequestParam(value = "lastParentCommentId", required = false) Long lastParentCommentId,
		@Parameter(description = "이전 페이지의 마지막 댓글 ID (첫 페이지는 생략)") @RequestParam(value = "lastCommentId", required = false) Long lastCommentId,
		@Parameter(description = "한 번에 가져올 댓글 수", required = true, example = "20") @RequestParam("pageSize") Long pageSize
	);

	@Operation(summary = "게시글별 댓글 총 개수 조회", description = "특정 게시글에 달린 전체 댓글 수를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "개수 조회 성공", content = @Content(schema = @Schema(implementation = Long.class))),
		@ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
	})
	Long count(
		@Parameter(description = "게시글 ID", required = true, example = "1") @PathVariable("articleId") Long articleId
	);

	@Operation(summary = "댓글 생성", description = "새로운 댓글을 등록합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "댓글 생성 성공", content = @Content(schema = @Schema(implementation = CommentResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 (입력값 검증 실패)"),
		@ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
	})
	CommentResponse create(
		@Parameter(description = "댓글 생성 정보") @RequestBody CommentCreateRequest request
	);

	@Operation(summary = "댓글 수정", description = "본인이 작성한 댓글을 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "댓글 수정 성공", content = @Content(schema = @Schema(implementation = CommentResponse.class))),
		@ApiResponse(responseCode = "403", description = "수정 권한 없음"),
		@ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없음")
	})
	CommentResponse update(
		@Parameter(description = "수정할 댓글의 ID", required = true, example = "1") @PathVariable("commentId") Long commentId,
		@Parameter(description = "수정할 내용") @RequestBody CommentUpdateRequest request
	);

	@Operation(summary = "댓글 삭제", description = "특정 댓글을 삭제합니다. (논리 삭제)")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "댓글 삭제 성공"),
		@ApiResponse(responseCode = "403", description = "삭제 권한 없음"),
		@ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없음")
	})
	void delete(
		@Parameter(description = "삭제할 댓글의 ID", required = true, example = "1") @PathVariable("commentId") Long commentId
	);
}
