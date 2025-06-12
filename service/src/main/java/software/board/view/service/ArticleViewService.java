package software.board.view.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.board.view.repository.ArticleViewCountRepository;

@Service
@RequiredArgsConstructor
public class ArticleViewService {

	private final ArticleViewCountRepository articleViewCountRepository;
	private final ArticleViewCountBackUpProcessor articleViewCountBackUpProcessor;

	private static final int BACKUP_BATCH_SIZE = 100;

	public Long increase(Long articleId) {
		Long count = articleViewCountRepository.increase(articleId);
		if (count % BACKUP_BATCH_SIZE == 0) {
			articleViewCountBackUpProcessor.backUp(articleId, count);
		}

		return count;
	}

	public Long count(Long articleId) {
		return articleViewCountRepository.read(articleId);
	}
}
