package com.skilllease.controllers;

import com.skilllease.dto.ResponseModel;
import com.skilllease.entities.Category;
import com.skilllease.services.CategoryService;
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
        return Response.ok(ResponseModel.builder().data(categoryService.getCategories()).build()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCategory(Category category) {
        return Response.ok().entity(ResponseModel.builder().data(categoryService.createCategory(category)).build()).build();
    }
}
