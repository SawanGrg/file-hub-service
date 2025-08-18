package dev.sawan.filehub.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@ConfigurationProperties(prefix = "aws.s3")
@Validated
@Getter
@Setter
public class S3Properties {

    @NotBlank(message = "S3 bucket name is required")
    private String bucketName;

    @NotBlank(message = "AWS region is required")
    private String region;

    private String accessKey;
    private String secretKey;

    // For LocalStack or custom S3-compatible services
    private String endpoint;

    // Path-style vs virtual-hosted-style URLs
    private boolean pathStyleAccess = false;

    @NotNull
    private String maxFileSize = "10MB";

    // List of allowed MIME types for uploads
    private List<String> allowedFileTypes;
}