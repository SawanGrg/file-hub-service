package dev.sawan.filehub.service;

import dev.sawan.filehub.config.S3Config;
import dev.sawan.filehub.config.S3Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;

@Service
public class S3ServiceImpl implements S3Service {
    private final S3Properties s3Properties;
    private final S3Client s3Client;

    @Autowired
    public S3ServiceImpl(S3Properties s3Properties, S3Client s3Client) {
        this.s3Properties = s3Properties;
        this.s3Client = s3Client;
    }

    @Override
    public String save(MultipartFile file) {
        try {
            String bucketName = s3Properties.getBucketName();
            String fileName = file.getOriginalFilename();

            // Validate file name
            if (fileName == null || fileName.isEmpty()) {
                throw new RuntimeException("File name cannot be null or empty");
            }

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

            // Return the S3 URL
            return String.format("https://%s.s3.%s.amazonaws.com/%s",
                    bucketName, s3Properties.getRegion(), fileName);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    public boolean delete(String fileName) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(s3Properties.getBucketName())
                    .key(fileName)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            return true; // Success

        } catch (S3Exception e) {
            // Log the error (consider using a proper logger)
            System.err.println("Failed to delete file from S3: " + e.getMessage());
            return false; // Failure
        }
    }
}