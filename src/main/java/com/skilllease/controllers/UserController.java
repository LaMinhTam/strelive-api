package com.skilllease.controllers;

import com.skilllease.dto.ProfilePictureForm;
import com.skilllease.dto.ResponseModel;
import com.skilllease.exception.AppException;
import com.skilllease.services.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import java.io.IOException;

@Path("users")
@ApplicationScoped
public class UserController {
    @Inject
    private UserService userService;

    @POST
    @Path("/profile-picture")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"EMPLOYER", "FREELANCER"})
    public Response uploadProfilePicture(@MultipartForm ProfilePictureForm form) throws AppException, IOException {
        return Response.ok(ResponseModel.builder()
                .data(userService.uploadProfilePicture(form))
                .build()).build();
    }
}
