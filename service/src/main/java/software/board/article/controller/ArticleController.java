package software.board.article.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import software.board.article.service.ArticleService;
import software.board.article.service.request.ArticleCreateRequest;
import software.board.article.service.request.ArticleUpdateRequest;
import software.board.article.service.response.ArticlePageResponse;
import software.board.article.service.response.ArticleResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ArticleController {

	private final ArticleService articleService;

	@GetMapping("/v1/articles/{articleId}")
	public ArticleResponse read(@PathVariable Long articleId) {
		return articleService.read(articleId);
	}

	@GetMapping("/v1/articles")
	public ArticlePageResponse readAll(
		@RequestParam("boardId") Long boardId,
		@RequestParam("page") Long page,
		@RequestParam("pageSize") Long pageSize
	) {
		return articleService.readAll(boardId, page, pageSize);
	}

	@PostMapping("/v1/articles")
	public ArticleResponse create(
		@RequestPart ArticleCreateRequest request,
		@RequestPart(required = false) List<MultipartFile> files) {
		return articleService.create(request, files);
	}

	@PutMapping("/v1/articles/{articleId}")
	public ArticleResponse update(
		@PathVariable("articleId") Long articleId,
		@RequestPart ArticleUpdateRequest request,
		@RequestPart(required = false) List<MultipartFile> newFiles,
		@RequestParam(required = false) List<Long> deletedFileIds) {
		return articleService.update(articleId, request, newFiles, deletedFileIds);
	}

	@DeleteMapping("/v1/articles/{articleId}")
	public void delete(@PathVariable("articleId") Long articleId) {
		articleService.delete(articleId);
	}
}
