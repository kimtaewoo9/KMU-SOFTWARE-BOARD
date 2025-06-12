package software.board.like.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import software.board.like.entity.ArticleLike;

@Repository
public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {

	Optional<ArticleLike> findByArticleIdAndUserId(Long articleId, Long userId);
}
