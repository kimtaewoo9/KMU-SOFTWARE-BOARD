package software.board.article.service;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageService {

	private final S3Client s3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	public String upload(MultipartFile file, String directoryPath) {
		if (file.isEmpty()) {
			throw new IllegalArgumentException("저장할 파일이 없습니다.");
		}

		try {
			String originalFilename = file.getOriginalFilename();
			String uniqueFileName = createUniqueFileName(originalFilename);

			PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(bucket)
				.key(uniqueFileName)
				.contentType(file.getContentType())
				.contentLength(file.getSize())
				.build();

			s3Client.putObject(putObjectRequest,
				RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

			return s3Client.utilities()
				.getUrl(b -> b.bucket(bucket).key(uniqueFileName)).toString();
		} catch (IOException e) {
			throw new RuntimeException("S3 파일 저장에 실패했습니다.", e);
		}
	}

	public void delete(String fileUrl) {
		try {
			URL url = new URL(fileUrl);
			String key = url.getPath().substring(1);

			DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
				.bucket(this.bucket) // 어느 버킷에서 삭제할지 지정
				.key(key)           // 어떤 객체(파일)를 삭제할지 지정
				.build();

			s3Client.deleteObject(deleteObjectRequest);

			log.info("[FileService.delete] successfully deleted file from s3 bucket");
		} catch (Exception e) {
			System.out.println("[FileService.delete] file delete failed");
		}
	}

	private String createUniqueFileName(String originalFilename) {
		String extension = getFileExtension(originalFilename);
		return UUID.randomUUID().toString() + extension;
	}

	private String getFileExtension(String fileName) {
		if (fileName == null || fileName.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "파일 이름이 유효하지 않습니다.");
		}
		try {
			return fileName.substring(fileName.lastIndexOf("."));
		} catch (StringIndexOutOfBoundsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
				"잘못된 형식의 파일(" + fileName + ") 입니다. 확장자를 포함해야 합니다.");
		}
	}
}
