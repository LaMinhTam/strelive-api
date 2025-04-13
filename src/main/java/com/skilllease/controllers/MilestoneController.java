package com.skilllease.controllers;

import com.skilllease.dto.*;
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"EMPLOYER", "FREELANCER"})
    public Response getMilestones(@QueryParam("JobId") Long jobId) {
        return Response.ok(ResponseModel.builder().data(milestoneService.findMilestonesByJobId(jobId)).build()).build();
    }

    @GET
    @Path("/contract/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"EMPLOYER", "FREELANCER"})
    public Response getMilestonesByContract(@PathParam("id") Long contractId) throws AppException {
        return Response.ok(ResponseModel.builder().data(milestoneService.findMilestonesByContractId(contractId)).build()).build();
    }

    @PUT
    @Path("/{id}/fulfill")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("FREELANCER")
    public Response fulfillMilestone(@PathParam("id") Long id, FulfillMilestoneDto dto) throws AppException {
        Milestone milestone = milestoneService.fulfillMilestone(id, dto);
        return Response.ok(ResponseModel.builder().data(milestone).build()).build();
    }

    @POST
    @Path("/{id}/fulfill/file")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("FREELANCER")
    public Response fulfillMilestoneWithFile(@PathParam("id") Long id,
                                             @MultipartForm FulfillMilestoneFileForm form) throws IOException, AppException {
        Milestone milestone = milestoneService.fulfillMilestoneWithFile(id, form);
        return Response.ok(ResponseModel.builder().data(milestone).build()).build();
    }

    // Employer reviews a milestone (approve or reject with feedback)
    @PUT
    @Path("/{id}/review")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("EMPLOYER")
    public Response reviewMilestone(@PathParam("id") Long id, MilestoneReviewDto dto) throws AppException {
        Milestone milestone = milestoneService.reviewMilestone(id, dto);
        return Response.ok(ResponseModel.builder().data(milestone).build()).build();
    }

    // Get milestone details by ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"EMPLOYER", "FREELANCER"})
    public Response getMilestone(@PathParam("id") Long id) throws AppException {
        Milestone milestone = milestoneService.getMilestoneById(id);
        return Response.ok(ResponseModel.builder().data(milestone).build()).build();
    }

    @PUT
    @Path("/{id}/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("EMPLOYER")
    public Response updateMilestone(@PathParam("id") Long id, UpdateMilestoneDto dto) throws AppException {
        Milestone milestone = milestoneService.updateMilestone(id, dto);
        return Response.ok(ResponseModel.builder().data(milestone).build()).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("EMPLOYER")
    public Response deleteMilestone(@PathParam("id") Long id) throws AppException {
        milestoneService.deleteMilestone(id);
        return Response.noContent().build();
    }
}
