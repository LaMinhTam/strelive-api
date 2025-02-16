package com.strelive.controllers;

import com.strelive.dto.RoleRequestDTO;
import com.strelive.services.RoleService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("roles")
public class RoleController {
    @Inject
    private RoleService roleService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRole(RoleRequestDTO roleRequestDTO) {
        return Response.status(Response.Status.CREATED).entity(roleService.createRole(roleRequestDTO)).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRole(@PathParam("id") Long id) {
        return Response.status(Response.Status.OK).entity(roleService.getRole(id)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRoles() {
        return Response.status(Response.Status.OK).entity(roleService.getAllRoles()).build();
    }
}
