package software.board.article.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import software.board.article.entity.ArticleFile;

@Repository
public interface ArticleFileRepository extends JpaRepository<ArticleFile, Long> {

	@Query(
		value = "SELECT * from article_file WHERE article_id = :articleId ORDER BY article_file_id ASC",
		nativeQuery = true
	)
	List<ArticleFile> findByArticleId(Long articleId);
}
