package com.skilllease.controllers;

import com.skilllease.dto.LoginRequestDTO;
import com.skilllease.dto.RefreshRequest;
import com.skilllease.dto.RegisterRequest;
import com.skilllease.dto.ResponseModel;
import com.skilllease.exception.InvalidTokenTypeException;
import com.skilllease.services.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("auth")
@ApplicationScoped
public class AuthController {
    @Inject
    private UserService userService;

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response registerUser(@Valid RegisterRequest registerRequest) {
        return Response.status(Response.Status.CREATED).entity(ResponseModel.builder()
                .data(userService.register(registerRequest))
                .build()).build();
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response authenticateUser(@Valid LoginRequestDTO loginRequestDTO) throws NotAuthorizedException {
        return Response.status(Response.Status.OK).entity(ResponseModel.builder()
                .data(userService.authenticate(loginRequestDTO))
                .build()).build();
    }

    @POST
    @Path("/refresh")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response refreshToken(@Valid RefreshRequest request) throws InvalidTokenTypeException {
        return Response.status(Response.Status.OK).entity(ResponseModel.builder()
                .data(userService.refreshToken(request))
                .build()).build();
    }
}
