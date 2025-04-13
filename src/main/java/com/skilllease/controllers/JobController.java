package com.skilllease.controllers;

import com.skilllease.dto.JobCreateDto;
import com.skilllease.dto.JobDto;
import com.skilllease.dto.ResponseModel;
import com.skilllease.entities.Job;
import com.skilllease.exception.AppException;
import com.skilllease.exception.EntityNotFoundException;
import com.skilllease.services.JobService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/jobs")
@ApplicationScoped
public class JobController {
    @Inject
    private JobService jobService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("EMPLOYER")
    public Response createJobPost(@Valid JobCreateDto job) {
        Job created = jobService.createJob(job);
        return Response.status(Response.Status.CREATED).entity(ResponseModel.builder().data(created).build()).build();
    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"EMPLOYER", "FREELANCER"})
    public Response getJob(@PathParam("id") Long id) throws AppException {
        return Response.ok(ResponseModel.builder().data(jobService.getJob(id)).build()).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"EMPLOYER", "FREELANCER"})
    public Response getJobsByEmployer(@QueryParam("employerId") Long employerId) {
        List<JobDto> jobs = (employerId != null)
                ? jobService.getJobsByEmployer(employerId)
                : jobService.getAllJobs();
        return Response.ok(ResponseModel.builder().data(jobs).build()).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("EMPLOYER")
    public Response updateJob(@PathParam("id") Long id, JobCreateDto job) throws EntityNotFoundException {
        return Response.ok(ResponseModel.builder().data(jobService.updateJob(id, job)).build()).build();
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("EMPLOYER")
    public Response deleteJob(@PathParam("id") Long id) {
        jobService.deleteJob(id);
        return Response.noContent().build();
    }
}
