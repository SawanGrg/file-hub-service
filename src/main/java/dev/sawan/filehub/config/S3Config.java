package dev.sawan.filehub.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@EnableConfigurationProperties(S3Properties.class)
public class S3Config {
    private final S3Properties s3Properties;

    @Autowired
    public S3Config(S3Properties s3Properties) {
        this.s3Properties = s3Properties;
    }

    @Bean
    public S3Client getS3Client(){
        return S3Client.builder()
                .region(Region.of(s3Properties.getRegion()))
                .credentialsProvider(getAwsCredentialProvider())
                .build();
    }

    @Bean
    public AwsCredentialsProvider getAwsCredentialProvider(){
        AwsBasicCredentials credentials = AwsBasicCredentials.create(s3Properties.getAccessKey(), s3Properties.getSecretKey());
        // Use StaticCredentialsProvider - this implements AwsCredentialsProvider
        return StaticCredentialsProvider.create(credentials);
    }
}