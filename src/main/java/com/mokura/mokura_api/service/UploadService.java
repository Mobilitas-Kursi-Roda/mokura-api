package com.mokura.mokura_api.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    String upload(MultipartFile file);
    String upload(MultipartFile file, MultipartFile[] files);
    String delete(String path);
}
