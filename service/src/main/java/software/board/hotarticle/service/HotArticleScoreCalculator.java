package software.board.hotarticle.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.board.like.entity.ArticleLikeCount;
import software.board.like.repository.ArticleLikeCountRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class HotArticleScoreCalculator {

	// TODO 좋아요가 10 개 이상이면 hot article 인걸로 변경 .
	private final ArticleLikeCountRepository articleLikeCountRepository;

	public long calculate(Long articleId) {
		
		ArticleLikeCount likeCount = articleLikeCountRepository.findLockedByArticleId(articleId)
			.orElseThrow(() -> new EntityNotFoundException(
				"[HotArticleScoreCalculator.ArticleLikeCountRepository] article like not found."));

		return likeCount.getLikeCount();
	}
}
