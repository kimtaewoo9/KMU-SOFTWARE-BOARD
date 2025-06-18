package software.board.like.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import software.board.like.service.response.ArticleLikeResponse;

@Tag(name = "좋아요 관리", description = "게시글 좋아요 관련 API")
public interface ArticleLikeApi {

	@Operation(summary = "좋아요 여부 조회", description = "특정 사용자가 특정 게시글에 좋아요를 눌렀는지 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공",
			content = @Content(schema = @Schema(implementation = ArticleLikeResponse.class))),
		@ApiResponse(responseCode = "404", description = "게시글 또는 사용자를 찾을 수 없음")
	})
	ArticleLikeResponse read(
		@Parameter(description = "게시글 ID", required = true, example = "1") @PathVariable("articleId") Long articleId,
		@Parameter(description = "사용자 ID", required = true, example = "1") @PathVariable("userId") Long userId
	);

	@Operation(summary = "게시글 좋아요 추가", description = "사용자가 게시글에 좋아요를 누릅니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "좋아요 처리 성공"),
		@ApiResponse(responseCode = "409", description = "이미 좋아요를 누른 상태"),
		@ApiResponse(responseCode = "404", description = "게시글 또는 사용자를 찾을 수 없음")
	})
	void like(
		@Parameter(description = "게시글 ID", required = true, example = "1") @PathVariable("articleId") Long articleId,
		@Parameter(description = "사용자 ID", required = true, example = "1") @PathVariable("userId") Long userId
	);

	@Operation(summary = "게시글 좋아요 취소", description = "사용자가 게시글에 누른 좋아요를 취소합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "좋아요 취소 성공"),
		@ApiResponse(responseCode = "404", description = "좋아요 정보 또는 게시글/사용자를 찾을 수 없음")
	})
	void unlike(
		@Parameter(description = "게시글 ID", required = true, example = "1") @PathVariable("articleId") Long articleId,
		@Parameter(description = "사용자 ID", required = true, example = "1") @PathVariable("userId") Long userId
	);

	@Operation(summary = "게시글 좋아요 총 개수 조회", description = "특정 게시글에 달린 좋아요의 전체 개수를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공",
			content = @Content(schema = @Schema(implementation = Long.class))),
		@ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
	})
	Long count(
		@Parameter(description = "게시글 ID", required = true, example = "1") @PathVariable("articleId") Long articleId
	);

	@Operation(summary = "비관적 락 1 - 좋아요 추가", description = "Pessimistic Lock (type 1)을 사용하여 좋아요를 추가합니다.")
	void likePessimisticLock1(
		@Parameter(description = "게시글 ID", required = true) @PathVariable("articleId") Long articleId,
		@Parameter(description = "사용자 ID", required = true) @PathVariable("userId") Long userId
	);

	@Operation(summary = "비관적 락 1 - 좋아요 취소", description = "Pessimistic Lock (type 1)을 사용하여 좋아요를 취소합니다.")
	void unlikePessimisticLock1(
		@Parameter(description = "게시글 ID", required = true) @PathVariable("articleId") Long articleId,
		@Parameter(description = "사용자 ID", required = true) @PathVariable("userId") Long userId
	);

	@Operation(summary = "비관적 락 2 - 좋아요 추가", description = "Pessimistic Lock (type 2)을 사용하여 좋아요를 추가합니다.")
	void likePessimisticLock2(
		@Parameter(description = "게시글 ID", required = true) @PathVariable("articleId") Long articleId,
		@Parameter(description = "사용자 ID", required = true) @PathVariable("userId") Long userId
	);

	@Operation(summary = "비관적 락 2 - 좋아요 취소", description = "Pessimistic Lock (type 2)을 사용하여 좋아요를 취소합니다.")
	void unlikePessimisticLock2(
		@Parameter(description = "게시글 ID", required = true) @PathVariable("articleId") Long articleId,
		@Parameter(description = "사용자 ID", required = true) @PathVariable("userId") Long userId
	);

	@Operation(summary = "낙관적 락 - 좋아요 추가", description = "Optimistic Lock을 사용하여 좋아요를 추가합니다.")
	void likeOptimisticLock(
		@Parameter(description = "게시글 ID", required = true) @PathVariable("articleId") Long articleId,
		@Parameter(description = "사용자 ID", required = true) @PathVariable("userId") Long userId
	);

	@Operation(summary = "낙관적 락 - 좋아요 취소", description = "Optimistic Lock을 사용하여 좋아요를 취소합니다.")
	void unlikeOptimisticLock(
		@Parameter(description = "게시글 ID", required = true) @PathVariable("articleId") Long articleId,
		@Parameter(description = "사용자 ID", required = true) @PathVariable("userId") Long userId
	);
}
