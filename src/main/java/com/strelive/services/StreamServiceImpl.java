package com.strelive.services;

import com.strelive.dto.StreamDTO;
import jakarta.ejb.Stateless;
import jakarta.inject.Named;

import java.util.Map;

@Stateless
public class StreamServiceImpl implements StreamService {
    @Override
    public StreamDTO validateStream(Map<String, String> map) {
        String key = map.getOrDefault("key", ""); // testKey is the key
        String streamId = map.getOrDefault("name", "");  // test is streamId

        return null;
    }

    @Override
    public StreamDTO validateStream(String key, String name) {
        return null;
    }

    @Override
    public StreamDTO publishDone(Map<String, String> map) {
        String streamId = map.getOrDefault("name", "");  // test is userId
        String streamName = map.get("name"); // The stream name
        String recordingPath = "/var/recordings/" + streamName + ".flv";

        return null;
    }
}
