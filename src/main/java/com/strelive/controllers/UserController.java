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
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Path("users")
public class UserController {
    @Inject
    private UserService userService;

    @POST
    @Path("profile-picture")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadProfilePicture(@Context HttpServletRequest request, MultipartFormDataInput input) {
        try {
            Long userId = Long.valueOf(DecodeToken.getSubjectToken(JwtTokenFilter.getCurrentToken(request)));

            // Get form data from the request
            Map<String, List<InputPart>> formData = input.getFormDataMap();

            // Extract the InputStream and file details
            List<InputPart> inputParts = formData.get("profilePicture"); // 'profilePicture' is the field name
            if (inputParts == null || inputParts.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("No file uploaded").build();
            }

            InputPart filePart = inputParts.get(0);

            // Extract the original filename
            String originalFileName = filePart.getHeaders().getFirst("Content-Disposition")
                    .split("filename=")[1].replace("\"", "").trim();

            InputStream fileInputStream = filePart.getBody(InputStream.class, null);

            boolean success = userService.saveProfilePicture(fileInputStream, originalFileName, userId);
            if (success) {
                return Response.ok().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
