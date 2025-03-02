package com.strelive.services.impl;

import com.strelive.dao.StreamDAO;
import com.strelive.dto.StreamCreateDTO;
import com.strelive.dto.StreamDTO;
import com.strelive.dto.VideoDTO;
import com.strelive.entities.User;
import com.strelive.services.StreamService;
import com.strelive.utils.ConfigUtils;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Stateless
public class StreamServiceImpl implements StreamService {
    private static final String HLS_DIRECTORY = ConfigUtils.getProperty("hls.directory");
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(StreamServiceImpl.class);
    private static final String BASE_VIDEO_URL = "http://localhost:9080/hls/";
    private static final String BASE_TS_URL = "http://localhost:8080/strelive-api/api/stream/ts/";
    @Inject
    private StreamDAO streamDAO;

    @Override
    public com.strelive.entities.Stream saveStream(User streamer, StreamCreateDTO streamCreateDTO) {
        String streamKey = UUID.randomUUID().toString();
        com.strelive.entities.Stream stream = new com.strelive.entities.Stream();
        stream.setTitle(streamCreateDTO.title());
        stream.setDescription(streamCreateDTO.description());
        stream.setStreamKey(streamKey);
        stream.setIsLive(false);
        stream.setCreatedAt(new Date());
        stream.setStreamer(streamer);
        return streamDAO.save(stream);
    }

    @Override
    public StreamDTO validateStream(String name) {
        Optional<com.strelive.entities.Stream> streamOpt = streamDAO.findByStreamKey(name);
        if (streamOpt.isPresent()) {
            com.strelive.entities.Stream stream = streamOpt.get();
            if (stream.getStartedAt() == null) {  // First time broadcasting
                stream.setStartedAt(new Date());
                stream.setIsLive(true);
                streamDAO.update(stream);
            }
        }
        return null;
    }

    @Override
    public StreamDTO publishDone(String name) {
        logger.info("Starting publishDone for key: {}, name: {}", name);
        File sourceFolder = new File(HLS_DIRECTORY);
        File destinationFolder = new File(HLS_DIRECTORY + name);

        if (!destinationFolder.exists() && !destinationFolder.mkdirs()) {
            logger.error("Failed to create directory: {}", destinationFolder.getAbsolutePath());
            return null;
        }
        moveFiles(sourceFolder, destinationFolder, name);

        logger.info("Completed publishDone for key: {}", name);
        return new StreamDTO("Stream processed successfully for key: " + name);
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
