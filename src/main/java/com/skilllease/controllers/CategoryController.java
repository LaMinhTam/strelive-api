package com.skilllease.controllers;

import com.skilllease.entities.Category;
import com.skilllease.services.CategoryService;
import com.skilllease.services.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("categories")
@ApplicationScoped
public class CategoryController {
    @Inject
    private CategoryService categoryService;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getCategories() {
        return Response.ok(categoryService.getCategories()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCategory(Category category) {
        return Response.ok().entity(categoryService.createCategory(category)).build();
    }
}
