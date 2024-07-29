package com.mokura.mokura_api.service.impl;

import com.mokura.mokura_api.service.UploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class UploadServiceImpl implements UploadService {
    @Override
    public String upload(MultipartFile file) {
        try {
            String uploadFolder = "uploads";
            // Ensure the upload folder exists
            Path uploadPath = Paths.get(uploadFolder);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate a unique filename using timestamp
            String originalFilename = file.getOriginalFilename();
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String uniqueFilename = timestamp + "-" + originalFilename;


            // Save the file
            byte[] bytes = file.getBytes();
            Path path = uploadPath.resolve(uniqueFilename);
            Files.write(path, bytes);

            return String.valueOf(path);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String upload(MultipartFile file, MultipartFile[] files) {
        return "";
    }

    @Override
    public String delete(String path) {
        return "";
    }
}
