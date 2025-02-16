package com.strelive.controllers;

import com.strelive.configurations.JwtTokenFilter;
import com.strelive.services.UserService;
import com.strelive.utils.DecodeToken;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.annotations.jaxrs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import java.io.InputStream;

@Path("users")
public class UserController {
    @Inject
    private UserService userService;

    @POST
    @Path("profile-picture")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadProfilePicture(@Context HttpServletRequest request, @MultipartForm InputStream profilePicture) {
        Long sub = Long.valueOf(DecodeToken.getSubjectToken(JwtTokenFilter.getCurrentToken(request)));
        boolean success = userService.saveProfilePicture(profilePicture, sub);
        if (success) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
