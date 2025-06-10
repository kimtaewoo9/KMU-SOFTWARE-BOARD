package software.board.article.repository;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.board.article.entity.Article;

@SpringBootTest
@Slf4j
class ArticleRepositoryTest {

	@Autowired
	ArticleRepository articleRepository;

	@Test
	void findAllTest() {
		List<Article> articles = articleRepository.findAll(1L, 30L, 30L);
		log.info("✅ articles.size: " + articles.size());
		for (Article article : articles) {
			log.info("article = {}", article);
		}
	}

	@Test
	void countTest() {
		Long count = articleRepository.count(1L, 10000L);
		log.info("count = {}", count);
	}

	@Test
	void findInfiniteScrollTest() {
		List<Article> firstPageArticles = articleRepository.findAllInfiniteScroll(1L, 30L);
		for (Article article : firstPageArticles) {
			log.info("articleId = {}", article.getArticleId());
		}

		Long lastArticleId = firstPageArticles.getLast().getArticleId();
		List<Article> nextPageArticles =
			articleRepository.findAllInfiniteScroll(1L, 30L, lastArticleId);
		log.info("[Next Page]");
		for (Article article : nextPageArticles) {
			log.info("articleId = {}", article.getArticleId());
		}
	}
}
