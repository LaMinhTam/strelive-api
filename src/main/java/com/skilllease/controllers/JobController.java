package com.skilllease.controllers;

import com.skilllease.dto.JobCreateDto;
import com.skilllease.dto.JobDto;
import com.skilllease.entities.Job;
import com.skilllease.exception.EntityNotFoundException;
import com.skilllease.mapper.JobMapper;
import com.skilllease.services.JobService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
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
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"EMPLOYER", "FREELANCER"})
    public Response getJob(@PathParam("id") Long id) throws EntityNotFoundException {
        return Response.ok(jobService.getJob(id)).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"EMPLOYER", "FREELANCER"})
    public Response getJobsByEmployer(@QueryParam("employerId") Long employerId) {
        List<Job> jobs = (employerId != null)
                ? jobService.getJobsByEmployer(employerId)
                : jobService.getAllJobs();
        return Response.ok(JobMapper.INSTANCE.toDtoList(jobs)).build();
    }


    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("EMPLOYER")
    public Response updateJob(@PathParam("id") Long id, JobCreateDto job) throws EntityNotFoundException {
        return Response.ok(jobService.updateJob(id, job)).build();
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
