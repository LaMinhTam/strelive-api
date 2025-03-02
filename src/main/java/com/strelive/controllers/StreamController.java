package com.strelive.controllers;

import com.strelive.dto.StreamDTO;
import com.strelive.services.StreamService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.File;

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
        var videoList = streamService.getAvailableVideos();
        return videoList.isEmpty() ?
                Response.status(Response.Status.NOT_FOUND).entity("No videos found").build() :
                Response.ok(videoList).build();
    }

    @GET
    @Path("/ts/{key}")
    @Produces("video/MP2T") // Serving .ts file as video
    public Response getTsSegment(@PathParam("key") String key) {
        File tsFile = new File(HLS_DIRECTORY + "/" + key);

        if (!tsFile.exists()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Segment not found").build();
        }

        return Response.ok(tsFile).build();
    }

}