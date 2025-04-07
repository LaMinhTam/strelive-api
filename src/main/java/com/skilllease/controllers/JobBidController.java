package com.skilllease.controllers;

import com.skilllease.dto.JobBidRequestDTO;
import com.skilllease.dto.JobBidUpdateDto;
import com.skilllease.dto.ResponseModel;
import com.skilllease.entities.JobBid;
import com.skilllease.exception.AppException;
import com.skilllease.services.JobBidService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/job-bids")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JobBidController {
    @Inject
    private JobBidService jobBidService;

    @POST
    @RolesAllowed("FREELANCER")
    public Response createJobBid(JobBidRequestDTO bidRequest) throws AppException {
        JobBid bid = jobBidService.createJobBid(bidRequest);
        return Response.status(Response.Status.CREATED).entity(ResponseModel.builder().data(bid).build()).build();
    }

    @GET
    @Path("/{id}")
    public Response getJobBid(@PathParam("id") Long id) throws AppException {
        return Response.ok(ResponseModel.builder().data(jobBidService.getJobBidById(id)).build()).build();
    }

    @GET
    @Path("/job/{jobId}")
    public Response getJobBidsByJob(@PathParam("jobId") Long jobId) {
        return Response.ok(ResponseModel.builder().data(jobBidService.getJobBidsByJob(jobId)).build()).build();
    }

    @PUT
    @Path("/{id}/status")
    @RolesAllowed("EMPLOYER")
    public Response updateJobBidStatus(@PathParam("id") Long id, JobBidUpdateDto jobBidUpdateDto) throws AppException {
        JobBid updatedBid = jobBidService.updateJobBidStatus(id, jobBidUpdateDto.getStatus());
        return Response.ok(ResponseModel.builder().data(updatedBid).build()).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("FREELANCER")
    public Response deleteJobBid(@PathParam("id") Long id) throws AppException {
        jobBidService.deleteJobBid(id);
        return Response.noContent().build();
    }
}
