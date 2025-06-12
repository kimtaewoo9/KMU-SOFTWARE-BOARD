package software.board.article.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.board.article.entity.Article;
import software.board.article.entity.ArticleFile;
import software.board.article.entity.BoardArticleCount;
import software.board.article.repository.ArticleFileRepository;
import software.board.article.repository.ArticleRepository;
import software.board.article.repository.BoardArticleCountRepository;
import software.board.article.service.request.ArticleCreateRequest;
import software.board.article.service.request.ArticleUpdateRequest;
import software.board.article.service.response.ArticlePageResponse;
import software.board.article.service.response.ArticleResponse;
import software.board.common.snowflake.Snowflake;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleService {

	private final Snowflake snowflake;
	private final ArticleRepository articleRepository;
	private final BoardArticleCountRepository boardArticleCountRepository;

	private final ArticleFileRepository articleFileRepository;
	private final FileStorageService fileStorageService;

	@Transactional
	public ArticleResponse create(ArticleCreateRequest request, List<MultipartFile> files) {
		Article article = Article.create(
			snowflake.nextId(),
			request.getTitle(),
			request.getContent(),
			request.getBoardId(),
			request.getWriterId());
		Article savedArticle = articleRepository.save(article);

		List<String> fileUrls = new ArrayList<>();
		if (files != null && !files.isEmpty()) {
			for (MultipartFile file : files) {
				String fileUrl = fileStorageService.upload(file, "articles");

				ArticleFile articleFile = ArticleFile.create(
					snowflake.nextId(),
					savedArticle.getArticleId(),
					fileUrl,
					file.getOriginalFilename(),
					file.getSize(),
					file.getContentType()
				);

				ArticleFile savedArticleFile = articleFileRepository.save(articleFile);
				fileUrls.add(savedArticleFile.getFileUrl());
			}
		}

		int result = boardArticleCountRepository.increase(request.getBoardId());
		if (result == 0) {
			boardArticleCountRepository.save(
				BoardArticleCount.init(request.getBoardId(), 1L)
			);
		}

		return ArticleResponse.from(savedArticle, fileUrls);
	}

	@Transactional
	public ArticleResponse update(Long articleId, ArticleUpdateRequest request,
		List<MultipartFile> newFiles, List<Long> deletedFileIds) {
		Article article = articleRepository.findById(articleId).orElseThrow(
			() -> new EntityNotFoundException("[ArticleService.update] article not found")
		);
		article.update(request.getTitle(), request.getContent());

		// 삭제할 파일들을 삭제함.
		if (deletedFileIds != null && !deletedFileIds.isEmpty()) {
			List<ArticleFile> files = articleFileRepository.findAllById(deletedFileIds);
			for (ArticleFile file : files) {
				if (file.getArticleId().equals(articleId)) {
					fileStorageService.delete(file.getFileUrl());
					articleFileRepository.delete(file);
				}
			}
		}
		// 새로 추가된 파일들을 처리함 .
		if (newFiles != null && !newFiles.isEmpty()) {
			List<ArticleFile> newArticleFiles = new ArrayList<>();
			for (MultipartFile newFile : newFiles) {
				// AWS S3 버킷에 저장 + 테이블에 ArticleFile 객체 저장
				String fileUrl = fileStorageService.upload(newFile, "articles");
				ArticleFile newArticleFile = ArticleFile.create(
					snowflake.nextId(),
					articleId,
					fileUrl,
					newFile.getOriginalFilename(),
					newFile.getSize(),
					newFile.getContentType()
				);
				newArticleFiles.add(newArticleFile);
			}
			articleFileRepository.saveAll(newArticleFiles);
		}
		List<ArticleFile> finalArticleFiles = articleFileRepository.findByArticleId(articleId);
		List<String> fileUrls = finalArticleFiles.stream().map(ArticleFile::getFileUrl).toList();

		return ArticleResponse.from(article, fileUrls);
	}

	@Transactional
	public void delete(Long articleId) {
		log.info("✅[ArticleService.delete]");

		Article article = articleRepository.findById(articleId).orElseThrow(
			() -> new EntityNotFoundException("[ArticleService.delete] article not found")
		);

		List<ArticleFile> articleFiles = articleFileRepository.findByArticleId(articleId);
		if (articleFiles != null && !articleFiles.isEmpty()) {
			for (ArticleFile articleFile : articleFiles) {
				fileStorageService.delete(articleFile.getFileUrl());
			}
			articleFileRepository.deleteAllInBatch(articleFiles);
		}
		articleRepository.delete(article);

		boardArticleCountRepository.decrease(article.getBoardId());
	}

	@Transactional(readOnly = true)
	public ArticleResponse read(Long articleId) {
		Article article = articleRepository.findById(articleId).orElseThrow(
			() -> new EntityNotFoundException("[ArticleService.read] article not found")
		);
		return ArticleResponse.from(article);
	}

	@Transactional(readOnly = true)
	public ArticlePageResponse readAll(Long boardId, Long page, Long pageSize) {
		List<ArticleResponse> articles = articleRepository.findAll(boardId, (page - 1) * pageSize,
				pageSize).stream()
			.map(ArticleResponse::from)
			.toList();

		Long count = articleRepository.count(
			boardId,
			PageLimitCalculator.calculatePageLimit(page, pageSize, 10L)
		);

		return ArticlePageResponse.of(articles, count);
	}

	@Transactional(readOnly = true)
	public List<ArticleResponse> readAllInfiniteScroll(Long boardId, Long pageSize,
		Long lastArticleId) {
		List<Article> articles = lastArticleId == null ?
			articleRepository.findAllInfiniteScroll(boardId, pageSize) :
			articleRepository.findAllInfiniteScroll(boardId, pageSize, lastArticleId);
		return articles.stream().map(ArticleResponse::from).toList();
	}

	@Transactional(readOnly = true)
	public Long count(Long boardId) {
		return boardArticleCountRepository.findById(boardId)
			.map(BoardArticleCount::getArticleCount)
			.orElse(0L);
	}
}
