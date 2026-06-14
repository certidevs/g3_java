package com.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileService {
    public static final String UPLOAD_DIR = "uploads";

    @Value("${spring.servlet.multipart.max-file-size:10MB}")
    private String maxFileSizeStr;

    public void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return;
        }

        long maxSizeBytes = DataSize.parse(maxFileSizeStr).toBytes();
        if (file.getSize() > maxSizeBytes) {
            throw new IllegalArgumentException("El archivo de imagen es demasiado grande. Máximo " + maxFileSizeStr + ".");
        }

        String fileName = file.getOriginalFilename();
        if (fileName != null) {
            String lower = fileName.toLowerCase();
            if (!lower.endsWith(".png") && !lower.endsWith(".jpg") && !lower.endsWith(".jpeg") && !lower.endsWith(".webp")) {
                throw new IllegalArgumentException("Solo se permiten imágenes PNG, JPG, JPEG o WEBP.");
            }
        }
    }

    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        validate(file);

        try {
            Path dir = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
            Files.createDirectories(dir);

            String original = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String ext = "";
            int dot = original.lastIndexOf('.');
            if (dot > 0) ext = original.substring(dot);
            String filename = UUID.randomUUID() + ext;

            file.transferTo(dir.resolve(filename));

            return filename;
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar el archivo", e);
        }
    }
}
