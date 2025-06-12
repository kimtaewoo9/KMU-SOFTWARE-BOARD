package software.board.comment.service;

import static java.util.function.Predicate.not;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import kuke.board.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.board.comment.entity.ArticleCommentCount;
import software.board.comment.entity.Comment;
import software.board.comment.repository.ArticleCommentCountRepository;
import software.board.comment.repository.CommentRepository;
import software.board.comment.service.request.CommentCreateRequest;
import software.board.comment.service.request.CommentUpdateRequest;
import software.board.comment.service.response.CommentPageResponse;
import software.board.comment.service.response.CommentResponse;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final Snowflake snowflake;

	private final CommentRepository commentRepository;
	private final ArticleCommentCountRepository articleCommentCountRepository;

	@Transactional
	public CommentResponse create(CommentCreateRequest request) {
		Comment parent = findParent(request);
		Comment newComment = Comment.create(
			snowflake.nextId(),
			request.getContent(),
			parent == null ? null : parent.getCommentId(),
			request.getArticleId(),
			request.getWriterId()
		);

		Comment savedComment = commentRepository.save(newComment);

		int result = articleCommentCountRepository.increase(request.getArticleId());
		if (result == 0) {
			articleCommentCountRepository.save(
				ArticleCommentCount.init(request.getArticleId(), 1L)
			);
		}

		return CommentResponse.from(savedComment);
	}

	private Comment findParent(CommentCreateRequest request) {
		Long parentCommentId = request.getParentCommentId();
		if (parentCommentId == null) {
			return null;
		}
		return commentRepository.findById(parentCommentId)
			.filter(not(Comment::getDeleted))
			.filter(Comment::isRoot)
			.orElseThrow();
	}

	@Transactional
	public CommentResponse update(Long commentId, CommentUpdateRequest request) {
		Comment comment = commentRepository.findById(commentId).orElseThrow(
			() -> new EntityNotFoundException("[CommentService.update] comment not found.")
		);

		comment.update(request.getContent());
		Comment savedComment = commentRepository.save(comment);

		return CommentResponse.from(savedComment);
	}

	@Transactional(readOnly = true)
	public CommentResponse read(Long commentId) {
		Comment comment = commentRepository.findById(commentId).orElseThrow(
			() -> new EntityNotFoundException("[CommentService.read] comment not found.")
		);

		return CommentResponse.from(comment);
	}

	@Transactional
	public void delete(Long commentId) {
		commentRepository.findById(commentId)
			.filter(not(Comment::getDeleted))
			.ifPresent(comment -> {
				if (hasChild(comment)) {
					comment.delete();
				} else {
					delete(comment);
				}
			});
	}

	private void delete(Comment comment) {
		commentRepository.delete(comment);
		articleCommentCountRepository.decrease(comment.getArticleId());

		// 삭제 후, 부모를 물리 삭제해아 하는지 확인 .. 논리 삭제 되어있고, 자식이 없으면 물리삭제
		if (!comment.isRoot()) {
			commentRepository.findById(comment.getParentCommentId())
				.filter(Comment::getDeleted)
				.filter(not(this::hasChild))
				.ifPresent(this::delete);
		}
	}

	private boolean hasChild(Comment comment) {
		return commentRepository.countBy(comment.getArticleId(), comment.getCommentId(), 2L) == 2;
	}

	@Transactional(readOnly = true)
	public CommentPageResponse readAll(Long articleId, Long page, Long pageSize) {
		List<CommentResponse> comments = commentRepository.findAll(articleId, (page - 1) * pageSize,
				pageSize).stream()
			.map(CommentResponse::from)
			.toList();

		Long commentCount = commentRepository
			.count(articleId, PageLimitCalculator.calculatePageLimit(page, pageSize, 10L));

		return CommentPageResponse.of(comments, commentCount);
	}

	@Transactional(readOnly = true)
	public List<CommentResponse> readAllInfiniteScroll(Long articleId, Long lastParentCommentId,
		Long lastCommentId, Long pageSize) {
		List<Comment> comments = lastParentCommentId == null || lastCommentId == null ?
			commentRepository.findAllInfiniteScroll(articleId, pageSize) :
			commentRepository.findAllInfiniteScroll(articleId, lastParentCommentId, lastCommentId,
				pageSize);
		return comments.stream().map(CommentResponse::from).toList();
	}

	@Transactional(readOnly = true)
	public Long count(Long articleId) {
		return articleCommentCountRepository.findById(articleId)
			.map(ArticleCommentCount::getCommentCount)
			.orElse(0L);
	}
}
