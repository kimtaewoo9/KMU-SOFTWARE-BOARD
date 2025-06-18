package software.board.hotarticle.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import software.board.hotarticle.service.response.HotArticleResponse;

@Tag(name = "인기 게시글 관리", description = "일자별 인기 게시글 조회 API")
public interface HotArticleApi {

	@Operation(summary = "일자별 인기 게시글 목록 조회", description = "특정 날짜의 인기 게시글 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공",
			content = @Content(schema = @Schema(type = "array", implementation = HotArticleResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 날짜 형식")
	})
	List<HotArticleResponse> readAll(
		@Parameter(description = "조회할 날짜 (yyyyMMdd 형식)", required = true, example = "20250618")
		@PathVariable String dateStr
	);
}
