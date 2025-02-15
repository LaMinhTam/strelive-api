package com.strelive.controllers;

import com.strelive.dto.LoginRequestDTO;
import com.strelive.dto.RefreshRequest;
import com.strelive.dto.RegisterRequest;
import com.strelive.dto.ResponseModel;
import com.strelive.services.UserService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("auth")
public class AuthController {
    @Inject
    private UserService userService;

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(@Valid RegisterRequest registerRequest) {
        return Response.status(Response.Status.CREATED).entity(ResponseModel.builder()
                .data(userService.register(registerRequest))
                .build()).build();
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(@Valid LoginRequestDTO loginRequestDTO) throws NotAuthorizedException {
        return Response.status(Response.Status.OK).entity(ResponseModel.builder()
                .data(userService.authenticate(loginRequestDTO))
                .build()).build();
    }

    @POST
    @Path("/refresh")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response refreshToken(@Valid RefreshRequest request) {
        return Response.status(Response.Status.OK).entity(ResponseModel.builder()
                .data(userService.refreshToken(request))
                .build()).build();
    }
}
