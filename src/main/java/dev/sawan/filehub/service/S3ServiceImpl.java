package dev.sawan.filehub.service;

import dev.sawan.filehub.config.S3Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

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

            // Build the PUT request (not the client!)
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            // Use the S3Client to execute the request
            PutObjectResponse response = s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

            // Return the S3 URL or key
            return String.format("https://%s.s3.amazonaws.com/%s", bucketName, fileName);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }
}