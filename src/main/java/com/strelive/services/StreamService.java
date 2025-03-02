package com.strelive.services;

import com.strelive.dto.StreamDTO;
import com.strelive.dto.VideoDTO;

import java.util.List;
import java.util.Map;

public interface StreamService {
    StreamDTO validateStream(Map<String,String> map);

    StreamDTO validateStream(String key, String name);

    StreamDTO publishDone(String key, String name);

    List<VideoDTO> getAvailableVideos();
}
