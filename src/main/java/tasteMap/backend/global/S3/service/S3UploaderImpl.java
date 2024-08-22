package tasteMap.backend.global.S3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3UploaderImpl implements S3Uploader {

    private final AmazonS3 amazonS3;

    @Value("${application.bucket.name}")
    private String bucket;


    @Override
    public void uploadCourse(MultipartFile file, Long id) {
        // 확장자를 제거한 파일 이름 생성
        String fileName = "course/" + id;
        // 파일을 S3에 업로드
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType()); // MIME 타입 설정

            amazonS3.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata));
        } catch (IOException e) {
            log.error("파일 업로드 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.");
        }
    }
    @Override
    public void uploadRoot(List<MultipartFile> files, Long id) {
        int index = 1;

        for (MultipartFile file : files) {
            // 확장자를 제거한 파일 이름 생성
            String fileName = "root/" + id +"/"+index;
            // 파일을 S3에 업로드
            try {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(file.getContentType()); // MIME 타입 설정
                metadata.setContentLength(file.getSize()); // 콘텐츠 길이 설정
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata));
                log.info("파일 업로드 성공: {}", fileName);
                index++;
            } catch (IOException e) {
                log.error("파일 업로드 중 오류 발생: {}", e.getMessage());
                throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
            }
        }
    }
}


