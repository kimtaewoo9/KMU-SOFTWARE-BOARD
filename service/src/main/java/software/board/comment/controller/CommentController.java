package software.board.comment.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.board.comment.service.CommentService;
import software.board.comment.service.request.CommentCreateRequest;
import software.board.comment.service.request.CommentUpdateRequest;
import software.board.comment.service.response.CommentPageResponse;
import software.board.comment.service.response.CommentResponse;

@RestController
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@GetMapping("/v1/comments/{commentId}")
	public CommentResponse read(
		@PathVariable("commentId") Long commentId
	) {
		return commentService.read(commentId);
	}

	@GetMapping("/v1/comments")
	public CommentPageResponse readAll(
		@RequestParam("articleId") Long articleId,
		@RequestParam("page") Long page,
		@RequestParam("pageSize") Long pageSize
	) {
		return commentService.readAll(articleId, page, pageSize);
	}

	@GetMapping("/v1/comments/infinite-scroll")
	public List<CommentResponse> readAllInfiniteScroll(
		@RequestParam("articleId") Long articleId,
		@RequestParam(value = "lastParentCommentId", required = false) Long lastParentCommentId,
		@RequestParam(value = "lastCommentId", required = false) Long lastCommentId,
		@RequestParam("pageSize") Long pageSize
	) {
		return commentService.readAllInfiniteScroll(articleId, lastParentCommentId,
			lastCommentId, pageSize);
	}

	@PostMapping("/v1/comments")
	public CommentResponse create(
		@RequestBody CommentCreateRequest request
	) {
		return commentService.create(request);
	}

	@PutMapping("/v1/comments/{commentId}")
	public CommentResponse update(
		@PathVariable("commentId") Long commentId,
		@RequestBody CommentUpdateRequest request
	) {
		return commentService.update(commentId, request);
	}

	@DeleteMapping("/v1/comments/{commentId}")
	public void delete(
		@PathVariable("commentId") Long commentId
	) {
		commentService.delete(commentId);
	}
}
