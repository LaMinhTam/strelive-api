package com.strelive.services.impl;

import com.strelive.dto.StreamDTO;
import com.strelive.services.StreamService;
import com.strelive.utils.ConfigUtils;
import jakarta.ejb.Stateless;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.stream.Stream;

@Stateless
public class StreamServiceImpl implements StreamService {
    private static final String HLS_DIRECTORY = ConfigUtils.getProperty("hls.directory");
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(StreamServiceImpl.class);
    private static final String apiHost = ConfigUtils.getProperty("thumbnail.api.host");

    @Override
    public StreamDTO validateStream(Map<String, String> map) {
        return null;
    }

    @Override
    public StreamDTO validateStream(String key, String name) {
        return null;
    }

    @Override
    public StreamDTO publishDone(String key, String name) {
        logger.info("Starting publishDone for key: {}, name: {}", key, name);
        File sourceFolder = new File(HLS_DIRECTORY);
        File destinationFolder = new File(HLS_DIRECTORY + key);

        if (!destinationFolder.exists() && !destinationFolder.mkdirs()) {
            logger.error("Failed to create directory: {}", destinationFolder.getAbsolutePath());
            return null;
        }
        moveFiles(sourceFolder, destinationFolder, key);
        generateThumbnail(key);

        logger.info("Completed publishDone for key: {}", key);
        return new StreamDTO("Stream processed successfully for key: " + key);
    }

    private void moveFiles(File sourceFolder, File destinationFolder, String key) {
        logger.info("Starting moveFiles for key: {}", key);
        File[] files = sourceFolder.listFiles((dir, filename) -> filename.startsWith(key));

        if (files != null) {
            for (File file : files) {
                File newFile = new File(destinationFolder, file.getName());
                file.renameTo(newFile);
            }
        }
        logger.info("Completed moveFiles for key: {}", key);
    }

    private void generateThumbnail(String key) {
        logger.info("Starting generateThumbnail for key: {}", key);

        String url = "http://" + apiHost + ":5000/generate-thumbnail";
        String jsonPayload = "{\"key\":\"" + key + "\"}";

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                logger.info("Thumbnail generated for key: {}", key);
            } else {
                logger.error("Failed to generate thumbnail: {}", response.body());
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Error calling thumbnail API", e);
        }

        logger.info("Completed generateThumbnail for key: {}", key);
    }
}
