package software.board.comment.service;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.board.comment.entity.Comment;
import software.board.comment.repository.CommentRepository;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

	@InjectMocks
	CommentService commentService;
	@Mock
	CommentRepository commentRepository;

	@Test
	@DisplayName("자식 댓글이 있는 부모 댓글은 논리적 삭제")
	void softDeleteTest() {
		Long articleId = 1L;
		Long commentId = 1L;
		Comment comment = createComment(articleId, commentId);

		given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));
		given(commentRepository.countBy(articleId, commentId, 2L)).willReturn(2L);

		// when
		commentService.delete(commentId);

		// then
		verify(comment).delete();
	}

	@Test
	@DisplayName("부모가 없는 댓글은 모두 물리적 삭제")
	void hardDeleteChildComment() {
		Long articleId = 1L;
		Long commentId = 2L;
		Long parentCommentId = 1L;

		Comment comment = createComment(articleId, commentId, parentCommentId);
		given(comment.isRoot()).willReturn(false);

		Comment parentComment = mock(Comment.class);
		given(parentComment.getDeleted()).willReturn(false);

		given(commentRepository.findById(commentId))
			.willReturn(Optional.of(comment));
		given(commentRepository.countBy(articleId, commentId, 2L))
			.willReturn(1L); // child는 자식이 없으므로 1L 반환 .

		given(commentRepository.findById(parentCommentId))
			.willReturn(Optional.of(parentComment));

		// when
		commentService.delete(comment.getCommentId());

		// then
		verify(commentRepository).delete(comment);
		verify(commentRepository, never()).delete(parentComment);
	}

	@Test
	@DisplayName("자식 댓글 삭제 시, 부모 댓글이 논리적 삭제 상태라면 재귀적으로 delete 되어야함.")
	void deletedParentShouldDeleteAllRecursively() {
		Long articleId = 1L;
		Long commentId = 2L;
		Long parentCommentId = 1L;

		Comment comment = createComment(articleId, commentId, parentCommentId);
		given(comment.isRoot()).willReturn(false);
		given(comment.getDeleted()).willReturn(false);

		Comment parentComment = createComment(articleId, parentCommentId);
		given(parentComment.isRoot()).willReturn(true);
		given(parentComment.getDeleted()).willReturn(true);

		given(commentRepository.findById(commentId))
			.willReturn(Optional.of(comment));
		given(commentRepository.countBy(articleId, commentId, 2L))
			.willReturn(1L); // 자식이 없음 .

		given(commentRepository.findById(parentCommentId))
			.willReturn(Optional.of(parentComment));
		given(commentRepository.countBy(articleId, parentCommentId, 2L))
			.willReturn(1L); // 이제 더이상 자식이 없어 . 그러면 soft delete

		// when
		commentService.delete(commentId);

		// then
		verify(commentRepository).delete(comment);
		verify(commentRepository).delete(parentComment);
		verify(comment).getDeleted();
		verify(parentComment).getDeleted();
	}

	// mock 객체 생성 .
	private Comment createComment(Long articleId, Long commentId) {
		Comment comment = mock(Comment.class);
		given(comment.getArticleId()).willReturn(articleId);
		given(comment.getCommentId()).willReturn(commentId);

		return comment;
	}

	private Comment createComment(Long articleId, Long commentId, Long parentCommentId) {
		Comment comment = mock(Comment.class);
		given(comment.getArticleId()).willReturn(articleId);
		given(comment.getCommentId()).willReturn(commentId);
		given(comment.getParentCommentId()).willReturn(parentCommentId);

		return comment;
	}
}
