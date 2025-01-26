package com.strelive.services;

import com.strelive.dto.StreamDTO;

import java.util.Map;

public interface StreamService {
    StreamDTO validateStream(Map<String,String> map);

    StreamDTO validateStream(String key, String name);

    StreamDTO publishDone(Map<String,String> map);
}
