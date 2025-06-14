package software.board.hotarticle.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Optional;
import java.util.random.RandomGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.board.like.entity.ArticleLikeCount;
import software.board.like.repository.ArticleLikeCountRepository;

@ExtendWith(MockitoExtension.class)
class HotArticleScoreCalculatorTest {

	@InjectMocks
	HotArticleScoreCalculator hotArticleScoreCalculator;

	@Mock
	ArticleLikeCountRepository articleLikeCountRepository;

	@Test
	void calculateTest() {
		// given
		Long articleId = 1L;
		Long likeCount = RandomGenerator.getDefault().nextLong(100);
		ArticleLikeCount articleLikeCount = mock(ArticleLikeCount.class);

		given(articleLikeCountRepository.findLockedByArticleId(articleId))
			.willReturn(Optional.of(articleLikeCount));
		given(articleLikeCount.getLikeCount()).willReturn(likeCount);

		// when
		long score = hotArticleScoreCalculator.calculate(articleId);

		// then
		assertThat(score).isEqualTo(likeCount);
	}
}
