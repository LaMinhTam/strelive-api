package com.strelive.controllers;

import com.strelive.dto.StreamCreateDTO;
import com.strelive.dto.StreamDTO;
import com.strelive.entities.Stream;
import com.strelive.entities.User;
import com.strelive.services.StreamService;
import com.strelive.utils.AuthUtils;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.File;

@Path("stream")
public class StreamController {
    @Inject
    private StreamService streamService;
    private static final String HLS_DIRECTORY = "C:\\Users\\Administrator\\LaMinhTam\\workspace\\intellij\\strelive-api\\hls_storage\\";
    @Context
    private HttpServletRequest request;

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("USER")
    public Response createStream(StreamCreateDTO streamCreateDTO) {
        User streamer = AuthUtils.getCurrentUser(request);  // Assume this retrieves the authenticated user
        if (streamer == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        Stream stream = streamService.saveStream(streamer, streamCreateDTO);
        return Response.ok(new StreamDTO(stream.getStreamKey())).build();
    }

    @POST
    @Path("/auth")
    @Consumes("application/x-www-form-urlencoded")
    @RolesAllowed("USER")
    public StreamDTO validateStream(@FormParam("name") String name) {
        return streamService.validateStream(name);
    }

    @POST
    @Path("/publish/done")
    @RolesAllowed("USER")
    public Response publishDone(@FormParam("key") String key, @FormParam("name") String name) {
        StreamDTO response = streamService.publishDone(name);
        if (response == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to process stream: " + key)
                    .build();
        }
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("USER")
    public Response getVideos() {
        var videoList = streamService.getAvailableVideos();
        return videoList.isEmpty() ?
                Response.status(Response.Status.NOT_FOUND).entity("No videos found").build() :
                Response.ok(videoList).build();
    }

    @GET
    @Path("/ts/{key}")
    @Produces("video/MP2T") // Serving .ts file as video
    @RolesAllowed("USER")
    public Response getTsSegment(@PathParam("key") String key) {
        File tsFile = new File(HLS_DIRECTORY + "/" + key);

        if (!tsFile.exists()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Segment not found").build();
        }

        return Response.ok(tsFile).build();
    }

}