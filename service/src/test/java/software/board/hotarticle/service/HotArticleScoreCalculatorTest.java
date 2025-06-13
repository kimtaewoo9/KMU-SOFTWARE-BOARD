package software.board.hotarticle.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.random.RandomGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.board.hotarticle.repository.ArticleCommentCountRepository;
import software.board.hotarticle.repository.ArticleLikeCountRepository;
import software.board.hotarticle.repository.ArticleViewCountRepository;

@ExtendWith(MockitoExtension.class)
class HotArticleScoreCalculatorTest {

	@InjectMocks
	HotArticleScoreCalculator hotArticleScoreCalculator;

	@Mock
	ArticleCommentCountRepository articleCommentCountRepository;

	@Mock
	ArticleLikeCountRepository articleLikeCountRepository;

	@Mock
	ArticleViewCountRepository articleViewCountRepository;

	@Test
	void calculateTest() {
		Long articleId = 1L;
		Long commentCount = RandomGenerator.getDefault().nextLong(100);
		Long likeCount = RandomGenerator.getDefault().nextLong(100);
		Long viewCount = RandomGenerator.getDefault().nextLong(100);

		given(articleCommentCountRepository.read(articleId)).willReturn(commentCount);
		given(articleLikeCountRepository.read(articleId)).willReturn(likeCount);
		given(articleViewCountRepository.read(articleId)).willReturn(viewCount);

		long score = hotArticleScoreCalculator.calculate(articleId);

		assertThat(score)
			.isEqualTo(10 * likeCount + commentCount + viewCount);
	}
}
