package com.strelive.controllers;

import com.strelive.dto.StreamDTO;
import com.strelive.services.StreamService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Path("stream")
public class StreamController {
    @Inject
    private StreamService streamService;
    private static final String HLS_DIRECTORY = "C:\\Users\\Administrator\\LaMinhTam\\workspace\\intellij\\strelive-api\\hls_storage\\";

    @POST
    @Path("/auth")
    @Consumes("application/x-www-form-urlencoded")
    public StreamDTO validateStream(@FormParam("key") String key, @FormParam("name") String name) {
        return streamService.validateStream(key, name);
    }

    @POST
    @Path("/publish/done")
    public Response publishDone(@FormParam("key") String key, @FormParam("name") String name) {
        StreamDTO response = streamService.publishDone(key, name);
        if (response == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to process stream: " + key)
                    .build();
        }
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVideos() {
        File folder = new File(HLS_DIRECTORY);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".m3u8"));

        if (files == null || files.length == 0) {
            return Response.status(Response.Status.NOT_FOUND).entity("No videos found").build();
        }

        List<String> videoList = new ArrayList<>();
        for (File file : files) {
            videoList.add("http://localhost:9080/hls/" + file.getName());
        }

        return Response.ok(videoList).build();
    }

    @GET
    @Path("/{filename}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getVideo(@PathParam("filename") String filename) {
        File file = new File(HLS_DIRECTORY + filename);

        if (!file.exists() || !filename.endsWith(".m3u8")) {
            return Response.status(Response.Status.NOT_FOUND).entity("Video not found").build();
        }

        String videoUrl = "http://localhost:9080/hls/" + filename;
        return Response.ok(videoUrl).build();
    }

}
