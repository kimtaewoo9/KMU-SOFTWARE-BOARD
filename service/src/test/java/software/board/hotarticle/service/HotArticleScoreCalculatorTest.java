package software.board.hotarticle.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.random.RandomGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.board.hotarticle.repository.HotArticleCommentCountRepository;
import software.board.hotarticle.repository.HotArticleLikeCountRepository;
import software.board.hotarticle.repository.HotArticleViewCountRepository;

@ExtendWith(MockitoExtension.class)
class HotArticleScoreCalculatorTest {

	@InjectMocks
	HotArticleScoreCalculator hotArticleScoreCalculator;

	@Mock
	HotArticleCommentCountRepository hotArticleCommentCountRepository;

	@Mock
	HotArticleLikeCountRepository hotArticleLikeCountRepository;

	@Mock
	HotArticleViewCountRepository hotArticleViewCountRepository;

	@Test
	void calculateTest() {
		Long articleId = 1L;
		Long commentCount = RandomGenerator.getDefault().nextLong(100);
		Long likeCount = RandomGenerator.getDefault().nextLong(100);
		Long viewCount = RandomGenerator.getDefault().nextLong(100);

		given(hotArticleCommentCountRepository.read(articleId)).willReturn(commentCount);
		given(hotArticleLikeCountRepository.read(articleId)).willReturn(likeCount);
		given(hotArticleViewCountRepository.read(articleId)).willReturn(viewCount);

		long score = hotArticleScoreCalculator.calculate(articleId);

		assertThat(score)
			.isEqualTo(10 * likeCount + commentCount + viewCount);
	}
}
