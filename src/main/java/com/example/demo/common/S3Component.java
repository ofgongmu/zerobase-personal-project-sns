package com.example.demo.common;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.ErrorCode;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class S3Component {
  private final AmazonS3 s3Client;
  private final String bucketName = "personal-project-sns-images";

  public String uploadFile(String folderName, MultipartFile file) {

    String fileName = createFileName(folderName);
    String contentType = getContentType(file.getOriginalFilename());

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType(contentType);

    try {
      s3Client.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata));
    } catch (IOException e) {
      throw new CustomException(ErrorCode.IMAGE_UPLOAD_FAILED);
    }

    return s3Client.getUrl(bucketName, fileName).toString();
  }

  private String createFileName(String folderName) {
    return folderName + UUID.randomUUID().toString() + LocalDateTime.now().toString();
  }

  private String getContentType(String originalFileName) {
    try {
      return originalFileName.split("\\.")[1];
    } catch (IndexOutOfBoundsException e) {
      throw new CustomException(ErrorCode.IMAGE_FORMAT_ERROR);
    }
  }
}
