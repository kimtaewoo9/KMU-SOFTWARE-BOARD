package software.board.like.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import software.board.like.service.ArticleLikeService;
import software.board.like.service.response.ArticleLikeResponse;

@RestController
@RequiredArgsConstructor
@Tag(name = "좋아요 관리 API")
public class ArticleLikeController {

	private final ArticleLikeService articleLikeService;

	@GetMapping("/v1/article-likes/articles/{articleId}/users/{userId}")
	public ArticleLikeResponse read(
		@PathVariable("articleId") Long articleId,
		@PathVariable("userId") Long userId
	) {
		return articleLikeService.read(articleId, userId);
	}

	@PostMapping("/v1/article-likes/articles/{articleId}/users/{userId}")
	public void like(
		@PathVariable("articleId") Long articleId,
		@PathVariable("userId") Long userId
	) {
		articleLikeService.like(articleId, userId);
	}

	@DeleteMapping("/v1/article-likes/articles/{articleId}/users/{userId}")
	public void unlike(
		@PathVariable("articleId") Long articleId,
		@PathVariable("userId") Long userId
	) {
		articleLikeService.unlike(articleId, userId);
	}

	@GetMapping("/v1/article-likes/articles/{articleId}/count")
	public Long count(
		@PathVariable("articleId") Long articleId
	) {
		return articleLikeService.count(articleId);
	}

	@PostMapping("/v1/article-likes/articles/{articleId}/users/{userId}/pessimistic-lock-1")
	public void likePessimisticLock1(
		@PathVariable("articleId") Long articleId,
		@PathVariable("userId") Long userId
	) {
		articleLikeService.likePessimisticLock1(articleId, userId);
	}

	@DeleteMapping("/v1/article-likes/aritlces/{articleId}/users/{userId}/pessimistic-lock-1")
	public void unlikePessimisticLock1(
		@PathVariable("articleId") Long articleId,
		@PathVariable("userId") Long userId
	) {
		articleLikeService.unlikePessimisticLock1(articleId, userId);
	}

	@PostMapping("/v1/article-likes/articles/{articleId}/users/{userId}/pessimistic-lock-2")
	public void likePessimisticLock2(
		@PathVariable("articleId") Long articleId,
		@PathVariable("userId") Long userId
	) {
		articleLikeService.likePessimisticLock2(articleId, userId);
	}

	@DeleteMapping("/v1/article-likes/articles/{articleId}/users/{userId}/pessimistic-lock-2")
	public void unlikePessimisticLock2(
		@PathVariable("articleId") Long articleId,
		@PathVariable("userId") Long userId
	) {
		articleLikeService.unlikePessimisticLock2(articleId, userId);
	}

	@PostMapping("/v1/article-likes/articles/{articleId}/users/{userId}/optimistic-lock")
	public void likeOptimisticLock(
		@PathVariable("articleId") Long articleId,
		@PathVariable("userId") Long userId
	) {
		articleLikeService.likeOptimisticLock(articleId, userId);
	}

	@DeleteMapping("/v1/article-likes/articles/{articleId}/users/{userId}/optimistic-lock")
	public void unlikeOptimisticLock(
		@PathVariable("articleId") Long articleId,
		@PathVariable("userId") Long userId
	) {
		articleLikeService.unlikeOptimisticLock(articleId, userId);
	}
}
