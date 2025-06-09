package software.board.article.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "article_file")
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleFile {

	@Id
	private Long articleFileId;
	private Long articleId;
	private String fileUrl;
	private String originalFilename;
	private Long fileSize;
	private String fileType;
	private LocalDateTime createdAt;

	public static ArticleFile create(Long articleFileId, Long articleId, String fileUrl,
		String originalFilename, Long fileSize, String fileType) {
		ArticleFile articleFile = new ArticleFile();
		articleFile.articleFileId = articleFileId;
		articleFile.articleId = articleId;
		articleFile.fileUrl = fileUrl;
		articleFile.originalFilename = originalFilename;
		articleFile.fileSize = fileSize;
		articleFile.fileType = fileType;
		articleFile.createdAt = LocalDateTime.now();

		return articleFile;
	}
}
