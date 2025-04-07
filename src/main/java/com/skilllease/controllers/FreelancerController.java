package com.skilllease.controllers;

import com.skilllease.dto.CvUploadForm;
import com.skilllease.dto.ResponseModel;
import com.skilllease.dto.UserDto;
import com.skilllease.exception.EntityNotFoundException;
import com.skilllease.mapper.FreelancerMapper;
import com.skilllease.services.FreelancerService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import java.io.IOException;

@Path("/freelancers")
@ApplicationScoped
public class FreelancerController {
    @Inject
    private FreelancerService freelancerService;

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"EMPLOYER", "FREELANCER"})
    public Response getFreelancer(@PathParam("id") Long id) throws EntityNotFoundException {
        var freelancer = freelancerService.findById(id);
        UserDto freelancerDto = FreelancerMapper.INSTANCE.toDto(freelancer);
        return Response.ok(ResponseModel.builder().data(freelancerDto).build()).build();
    }

    @POST
    @Path("/{id}/cv")
    @PermitAll
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("FREELANCER")
    public Response uploadCv(@PathParam("id") Long id, @MultipartForm CvUploadForm form) throws IOException, EntityNotFoundException {
        return Response.ok(ResponseModel.builder().data(freelancerService.uploadCv(id, form)).build()).build();
    }
}
