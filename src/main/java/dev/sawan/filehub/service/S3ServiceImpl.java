package dev.sawan.filehub.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3ServiceImpl implements S3Service{
    @Override
    public String save(MultipartFile file) {
        return "";
    }
}
