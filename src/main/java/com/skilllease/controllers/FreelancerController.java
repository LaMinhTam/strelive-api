package com.skilllease.controllers;

import com.skilllease.dto.CvUploadForm;
import com.skilllease.dto.ServiceDto;
import com.skilllease.dto.UserDto;
import com.skilllease.entities.Service;
import com.skilllease.entities.User;
import com.skilllease.exception.EntityNotFoundException;
import com.skilllease.mapper.FreelancerMapper;
import com.skilllease.services.FreelancerService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import java.io.IOException;
import java.util.List;

@Path("/freelancers")
@Produces(MediaType.APPLICATION_JSON)
public class FreelancerController {

    @Inject
    private FreelancerService freelancerService;

    @GET
    @Path("/{id}")
    public Response getFreelancer(@PathParam("id") Long id) throws EntityNotFoundException {
        var freelancer = freelancerService.findById(id);
        UserDto freelancerDto = FreelancerMapper.INSTANCE.toDto(freelancer);
        return Response.ok(freelancerDto).build();
    }

    @POST
    @Path("/{id}/cv")
    @PermitAll
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadCv(@PathParam("id") Long id, @MultipartForm CvUploadForm form) throws IOException, EntityNotFoundException {
        return Response.ok(freelancerService.uploadCv(id, form)).build();
    }

    @POST
    @Path("/{id}/services")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("FREELANCER")
    public Response addService(@PathParam("id") Long id, ServiceDto service) throws EntityNotFoundException {
        Service createdService = freelancerService.addService(id, service);
        return Response.status(Response.Status.CREATED).entity(createdService).build();
    }
}
