package com.example.gachisikyeo_be.app.controller.awsS3;


import com.example.gachisikyeo_be.app.service.awsS3.AwsS3Service;
import com.example.gachisikyeo_be.global.code.SuccessCode;
import com.example.gachisikyeo_be.global.responseTemplate.ApiResponseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class AmazonS3Controller {

    private final AwsS3Service awsS3Service;

    /**
     * 파일 업로드
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseTemplate<List<String>>> uploadFile(
            @RequestPart("files") List<MultipartFile> files
    ) {
        return ApiResponseTemplate.success(
                SuccessCode.FILE_UPLOAD_SUCCESS,
                awsS3Service.uploadFile(files)
        );
    }

    /**
     * 파일 삭제
     */
    @DeleteMapping
    public ResponseEntity<ApiResponseTemplate<String>> deleteFile(
            @RequestParam String fileName
    ) {
        awsS3Service.deleteFile(fileName);

        return ApiResponseTemplate.success(
                SuccessCode.FILE_DELETE_SUCCESS,
                fileName
        );
    }
}
