package com.skilllease.controllers;

import com.skilllease.dto.JobBidRequestDTO;
import com.skilllease.dto.JobBidUpdateDto;
import com.skilllease.entities.JobBid;
import com.skilllease.exception.AppException;
import com.skilllease.exception.EntityNotFoundException;
import com.skilllease.services.JobBidService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

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
        return Response.status(Response.Status.CREATED).entity(bid).build();
    }

    @GET
    @Path("/{id}")
    public Response getJobBid(@PathParam("id") Integer id) {
        Optional<JobBid> bidOpt = jobBidService.getJobBidById(id);
        return bidOpt.map(Response::ok)
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @GET
    @Path("/job/{jobId}")
    public Response getJobBidsByJob(@PathParam("jobId") Long jobId) {
        List<JobBid> bids = jobBidService.getJobBidsByJob(jobId);
        return Response.ok(bids).build();
    }

    @PUT
    @Path("/{id}/status")
    @RolesAllowed("EMPLOYER")
    public Response updateJobBidStatus(@PathParam("id") Integer id, JobBidUpdateDto jobBidUpdateDto) throws AppException {
        JobBid updatedBid = jobBidService.updateJobBidStatus(id, jobBidUpdateDto.getStatus());
        return Response.ok(updatedBid).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("FREELANCER")
    public Response deleteJobBid(@PathParam("id") Integer id) throws AppException {
        jobBidService.deleteJobBid(id);
        return Response.noContent().build();
    }
}
