package com.skilllease.controllers;

import com.skilllease.dto.ResponseModel;
import com.skilllease.dto.ReviewRequestDTO;
import com.skilllease.entities.Review;
import com.skilllease.exception.AppException;
import com.skilllease.services.ReviewService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/reviews")
@ApplicationScoped
public class ReviewController {
    @Inject
    private ReviewService reviewService;

    // Endpoint for submitting a review.
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"EMPLOYER", "FREELANCER"})
    public Response createReview(ReviewRequestDTO reviewRequestDTO) throws AppException {
        Review review = reviewService.createReview(reviewRequestDTO);
        return Response.status(Response.Status.CREATED).entity(ResponseModel.builder().data(review).build()).build();
    }

    // Endpoint for retrieving reviews for a specific user.
    @GET
    @Path("/user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReviewsByUser(@PathParam("userId") Long userId) {
        List<Review> reviews = reviewService.getReviewsByUser(userId);
        return Response.ok(ResponseModel.builder().data(reviews).build()).build();
    }

    @GET
    @Path("/contract/{contractId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReviewsByContract(@PathParam("contractId") Long contractId) {
        List<Review> reviews = reviewService.getReviewsByContract(contractId);
        return Response.ok(ResponseModel.builder().data(reviews).build()).build();
    }
}
