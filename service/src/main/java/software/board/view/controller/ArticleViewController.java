package software.board.view.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import software.board.view.service.ArticleViewService;

@RestController
@RequiredArgsConstructor
@Tag(name = "조회수 관리", description = "조회수 관련 API")
public class ArticleViewController {

	private final ArticleViewService articleViewService;

	@GetMapping("/v1/article-views/articles/{articleId}/count")
	public Long count(
		@PathVariable("articleId") Long articleId
	) {
		return articleViewService.count(articleId);
	}

	@PostMapping("/v1/article-views/articles/{articleId}")
	public Long increase(
		@PathVariable("articleId") Long articleId
	) {
		return articleViewService.increase(articleId);
	}
}
