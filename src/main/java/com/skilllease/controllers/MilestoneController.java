package com.skilllease.controllers;

import com.skilllease.dto.CreateMilestoneDto;
import com.skilllease.dto.MilestoneFileForm;
import com.skilllease.dto.MilestoneReviewDto;
import com.skilllease.entities.Milestone;
import com.skilllease.exception.AppException;
import com.skilllease.services.MilestoneService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import java.io.IOException;

@Path("/milestones")
@ApplicationScoped
public class MilestoneController {
    @Inject
    private MilestoneService milestoneService;

    // Create milestone submission using JSON (for LINK or PREVIEW submissions)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("FREELANCER")
    public Response createMilestone(CreateMilestoneDto dto) throws AppException {
        Milestone milestone = milestoneService.createMilestone(dto);
        return Response.status(Response.Status.CREATED).entity(milestone).build();
    }

    // Create milestone submission with a file upload
    @POST
    @Path("/file")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("FREELANCER")
    public Response createMilestoneWithFile(@MultipartForm MilestoneFileForm form) throws IOException, AppException {
        Milestone milestone = milestoneService.createMilestoneWithFile(form);
        return Response.status(Response.Status.CREATED).entity(milestone).build();
    }

    // Employer reviews a milestone (approve or reject with feedback)
    @PUT
    @Path("/{id}/review")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("EMPLOYER")
    public Response reviewMilestone(@PathParam("id") Long id, MilestoneReviewDto dto) throws AppException {
        Milestone milestone = milestoneService.reviewMilestone(id, dto);
        return Response.ok(milestone).build();
    }

    // Get milestone details by ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"EMPLOYER", "FREELANCER"})
    public Response getMilestone(@PathParam("id") Long id) {
        Milestone milestone = milestoneService.getMilestoneById(id);
        return Response.ok(milestone).build();
    }
}
