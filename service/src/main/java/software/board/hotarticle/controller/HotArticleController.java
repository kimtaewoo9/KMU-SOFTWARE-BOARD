package software.board.hotarticle.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import software.board.hotarticle.service.HotArticleService;
import software.board.hotarticle.service.response.HotArticleResponse;

@RestController
@RequiredArgsConstructor
@Tag(name = "인기 게시글 관리", description = "인기 게시글 관련 API")
public class HotArticleController {

	private final HotArticleService hotArticleService;

	@GetMapping("/v1/hot-articles/articles/date/{dateStr}")
	public List<HotArticleResponse> readAll(
		@PathVariable String dateStr
	) {
		return hotArticleService.getHotArticleList(dateStr);
	}
}
