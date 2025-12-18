package com.example.gachisikyeo_be.app.service.awsS3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsS3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * ✅ 여러 파일 업로드 (AmazonS3Controller용)
     */
    public List<String> uploadFile(List<MultipartFile> files) {

        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }

        List<String> uploadUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            uploadUrls.add(uploadSingleImage(file));
        }

        return uploadUrls;
    }

    /**
     * ✅ 이미지 1장 필수 업로드 (상품 등록용)
     */
    public String uploadSingleImage(MultipartFile imageFile) {

        // 1️⃣ 필수 체크
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("상품 이미지는 반드시 1장 업로드해야 합니다.");
        }

        // 2️⃣ 파일명 생성
        String originalFilename = imageFile.getOriginalFilename();
        String extension = getExtension(originalFilename);

        String fileName = "product/"
                + UUID.randomUUID()
                + extension;

        try {
            // 3️⃣ 메타데이터 설정
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(imageFile.getSize());
            metadata.setContentType(imageFile.getContentType());

            // 4️⃣ S3 업로드 (❗ ACL 절대 사용 안 함)
            amazonS3.putObject(
                    new PutObjectRequest(
                            bucket,
                            fileName,
                            imageFile.getInputStream(),
                            metadata
                    )
            );

        } catch (IOException e) {
            log.error("S3 이미지 업로드 실패", e);
            throw new IllegalStateException("이미지 업로드 중 오류가 발생했습니다.");
        }

        // 5️⃣ 접근 가능한 URL 반환
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    /**
     * ✅ 파일 삭제
     */
    public void deleteFile(String fileName) {
        amazonS3.deleteObject(bucket, fileName);
    }

    /**
     * 확장자 추출
     */
    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new IllegalArgumentException("잘못된 파일 형식입니다.");
        }
        return filename.substring(filename.lastIndexOf("."));
    }
}
