package dev.sawan.filehub.service;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {

    String save(
            MultipartFile file
    );
}
