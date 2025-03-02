package com.strelive.services.impl;

import com.strelive.dto.StreamDTO;
import com.strelive.dto.VideoDTO;
import com.strelive.services.StreamService;
import com.strelive.utils.ConfigUtils;
import jakarta.ejb.Stateless;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Stateless
public class StreamServiceImpl implements StreamService {
    private static final String HLS_DIRECTORY = ConfigUtils.getProperty("hls.directory");
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(StreamServiceImpl.class);
    private static final String BASE_VIDEO_URL = "http://localhost:9080/hls/";
    private static final String BASE_TS_URL = "http://localhost:8080/strelive-api/api/stream/ts/";

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

    @Override
    public List<VideoDTO> getAvailableVideos() {
        try (Stream<Path> paths = Files.walk(Paths.get(HLS_DIRECTORY))) {
            return paths
                    .filter(this::isM3u8File)
                    .parallel()
                    .map(this::processM3u8File)
                    .filter(Objects::nonNull)
                    .toList();
        } catch (IOException e) {
            logger.error("Error retrieving available videos", e);
            return Collections.emptyList();
        }
    }

    private boolean isM3u8File(Path path) {
        return Files.isRegularFile(path) && path.toString().endsWith(".m3u8");
    }

    private VideoDTO processM3u8File(Path m3u8Path) {
        try (Stream<String> lines = Files.lines(m3u8Path)) {
            List<String> tsFiles = lines.filter(line -> line.endsWith(".ts"))
                    .collect(Collectors.toList());

            if (tsFiles.isEmpty()) {
                return null;
            }

            String videoUrl = BASE_VIDEO_URL + m3u8Path.getFileName().toString();
            String middleFile = tsFiles.get(tsFiles.size() / 2);
            String middleTsUrl = BASE_TS_URL + middleFile;

            VideoDTO videoDTO = new VideoDTO();
            videoDTO.setVideoUrl(videoUrl);
            videoDTO.setMiddleTsUrl(middleTsUrl);
            return videoDTO;
        } catch (IOException e) {
            logger.error("Error processing m3u8 file: {}", m3u8Path, e);
            return null;
        }
    }
}
