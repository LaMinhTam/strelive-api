package com.skilllease.controllers;

import com.skilllease.dto.ResponseModel;
import com.skilllease.dto.ServiceCreateDto;
import com.skilllease.entities.Service;
import com.skilllease.exception.EntityNotFoundException;
import com.skilllease.services.ServiceService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/services")
@ApplicationScoped
public class ServiceController {
    @Inject
    private ServiceService serviceService;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getServices(@QueryParam("freelancerId") Long freelancerId) {
        List<Service> services;
        if (freelancerId != null) {
            services = serviceService.getServiceByUserId(freelancerId);
        } else {
            services = serviceService.getAllServices();
        }
        return Response.ok(ResponseModel.builder().data(services).build()).build();
    }

    @GET
    @Path("/{serviceId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getService(@PathParam("serviceId") Long serviceId) throws EntityNotFoundException {
        return Response.ok(ResponseModel.builder().data(serviceService.getServiceById(serviceId)).build()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("FREELANCER")
    public Response addService(ServiceCreateDto service) throws EntityNotFoundException {
        Service createdService = serviceService.addService(service);
        return Response.status(Response.Status.CREATED).entity(ResponseModel.builder().data(createdService).build()).build();
    }

    @PUT
    @Path("/{serviceId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("FREELANCER")
    public Response updateService(@PathParam("serviceId") Long serviceId, ServiceCreateDto service) throws EntityNotFoundException {
        Service updatedService = serviceService.updateService(serviceId, service);
        return Response.ok(ResponseModel.builder().data(updatedService).build()).build();
    }
}
