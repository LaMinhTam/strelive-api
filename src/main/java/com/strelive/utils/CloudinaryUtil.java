package com.strelive.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class CloudinaryUtil {
    private static final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", ConfigUtils.getProperty("cloudiary.cloud_name"),
            "api_key", ConfigUtils.getProperty("cloudiary.api_key"),
            "api_secret", ConfigUtils.getProperty("cloudiary.api_secret")
    ));

    public static String uploadImage(File file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(
                file,
                ObjectUtils.asMap("resource_type", "image")
        );
        return (String) uploadResult.get("secure_url");
    }
}
