package com.strelive.services;

import com.strelive.dto.StreamCreateDTO;
import com.strelive.dto.StreamDTO;
import com.strelive.dto.VideoDTO;
import com.strelive.entities.User;

import java.util.List;
import java.util.Map;

public interface StreamService {
    com.strelive.entities.Stream saveStream(User user, StreamCreateDTO stream);

    StreamDTO validateStream(String name);

    StreamDTO publishDone(String name);

    List<VideoDTO> getAvailableVideos();
}
