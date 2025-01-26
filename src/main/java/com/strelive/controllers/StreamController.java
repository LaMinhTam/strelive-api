package com.strelive.controllers;

import com.strelive.dto.StreamDTO;
import com.strelive.services.StreamService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import java.util.Map;

@Path("stream")
public class StreamController {
    @Inject
    private StreamService streamService;

    @POST
    @Path("/auth")
    @Consumes("application/x-www-form-urlencoded")
    public StreamDTO validateStream(@FormParam("key") String key, @FormParam("name") String name){
        return streamService.validateStream(key, name);
    }

    @POST
    @Path("/publish/done")
    public StreamDTO publishDone(Map<String, String> body) {
        return streamService.publishDone(body);
    }
}
