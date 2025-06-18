package software.board.article.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import software.board.article.service.request.ArticleCreateRequest;
import software.board.article.service.request.ArticleUpdateRequest;
import software.board.article.service.response.ArticlePageResponse;
import software.board.article.service.response.ArticleResponse;

@Tag(name = "게시글 관리", description = "게시글 관련 API")
public interface ArticleApi {

	@Operation(summary = "게시글 단건 조회", description = "특정 게시글의 상세 정보를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "게시글 조회 성공",
			content = @Content(schema = @Schema(implementation = ArticleResponse.class))),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 게시글"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	ArticleResponse read(
		@Parameter(description = "조회할 게시글의 ID", required = true, example = "1")
		@PathVariable Long articleId
	);

	@Operation(summary = "게시글 목록 페이지네이션 조회", description = "특정 게시판의 게시글 목록을 페이지 단위로 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공",
			content = @Content(schema = @Schema(implementation = ArticlePageResponse.class),
				examples = @ExampleObject(value = """
					{
					  "articles": [
					    {
					      "articleId": 1,
					      "title": "예시 게시글",
					      "content": "이것은 예시 내용입니다."
					    }
					  ],
					  "articleCount": 100
					}
					"""))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	ArticlePageResponse readAll(
		@Parameter(description = "게시판 ID", required = true, example = "1") @RequestParam("boardId") Long boardId,
		@Parameter(description = "페이지 번호 (1부터 시작)", required = true, example = "1") @RequestParam("page") Long page,
		@Parameter(description = "한 페이지에 보여줄 게시글 수", required = true, example = "10") @RequestParam("pageSize") Long pageSize
	);

	@Operation(summary = "게시판별 게시글 총 개수 조회", description = "특정 게시판의 전체 게시글 수를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "개수 조회 성공", content = @Content(schema = @Schema(implementation = Long.class))),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 게시판"),
	})
	Long count(
		@Parameter(description = "게시판 ID", required = true, example = "1") @PathVariable("boardId") Long boardId
	);

	@Operation(summary = "게시글 목록 무한 스크롤 조회", description = "커서 기반으로 다음 게시글 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공",
			content = @Content(schema = @Schema(type = "array", implementation = ArticleResponse.class)))
	})
	List<ArticleResponse> readAllInfiniteScroll(
		@Parameter(description = "게시판 ID", required = true, example = "1") @RequestParam("boardId") Long boardId,
		@Parameter(description = "한 번에 가져올 게시글 수", required = true, example = "10") @RequestParam("pageSize") Long pageSize,
		@Parameter(description = "이전 페이지의 마지막 게시글 ID (첫 페이지는 생략)") @RequestParam(value = "lastArticleId", required = false) Long lastArticleId
	);

	@Operation(summary = "게시글 생성", description = "새로운 게시글을 파일과 함께 등록합니다. (multipart/form-data)")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "게시글 생성 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청")
	})
	ArticleResponse create(
		@Parameter(description = "게시글 생성 정보 (JSON 형식)", required = true) @RequestPart("request") ArticleCreateRequest request,
		@Parameter(description = "업로드할 파일 목록") @RequestPart(value = "files", required = false) List<MultipartFile> files
	);

	@Operation(summary = "게시글 수정", description = "기존 게시글의 내용을 수정하고 파일을 추가 또는 삭제합니다. (multipart/form-data)")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "게시글 수정 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class))),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 게시글"),
		@ApiResponse(responseCode = "403", description = "수정 권한 없음")
	})
	ArticleResponse update(
		@Parameter(description = "수정할 게시글의 ID", required = true, example = "1") @PathVariable("articleId") Long articleId,
		@Parameter(description = "게시글 수정 정보 (JSON 형식)", required = true) @RequestPart ArticleUpdateRequest request,
		@Parameter(description = "새롭게 추가할 파일 목록") @RequestPart(required = false) List<MultipartFile> newFiles,
		@Parameter(description = "삭제할 기존 파일들의 ID 목록") @RequestParam(required = false) List<Long> deletedFileIds
	);

	@Operation(summary = "게시글 삭제", description = "특정 게시글을 삭제합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "게시글 삭제 성공"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 게시글"),
		@ApiResponse(responseCode = "403", description = "삭제 권한 없음")
	})
	void delete(
		@Parameter(description = "삭제할 게시글의 ID", required = true, example = "1") @PathVariable("articleId") Long articleId
	);
}
