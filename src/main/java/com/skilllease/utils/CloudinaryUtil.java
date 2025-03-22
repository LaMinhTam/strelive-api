package com.skilllease.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;

public class CloudinaryUtil {
    private static final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", ConfigUtils.getProperty("cloudiary.cloud_name"),
            "api_key", ConfigUtils.getProperty("cloudiary.api_key"),
            "api_secret", ConfigUtils.getProperty("cloudiary.api_secret")
    ));

    public static String uploadFile(InputStream inputStream, String fileName) throws IOException {
        // Create a temporary file from the InputStream
        Path tempFile = Files.createTempFile("cv-upload-", fileName != null ? fileName : ".tmp");
        try {
            // Copy InputStream to temporary file
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            // Upload to Cloudinary
            Map uploadResult = cloudinary.uploader().upload(
                    tempFile.toFile(),
                    ObjectUtils.asMap("resource_type", "auto") // Auto-detect file type (PDF, docx, etc.)
            );
            return (String) uploadResult.get("secure_url");
        } finally {
            // Clean up the temporary file
            Files.deleteIfExists(tempFile);
        }
    }
}