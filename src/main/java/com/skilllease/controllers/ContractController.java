package com.skilllease.controllers;

import com.skilllease.dto.ContractStatusUpdateDto;
import com.skilllease.dto.CreateContractDto;
import com.skilllease.dto.ResponseModel;
import com.skilllease.entities.Contract;
import com.skilllease.exception.AppException;
import com.skilllease.services.ContractService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/contracts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ContractController {
    @Inject
    private ContractService contractService;

    // Create a new contract (only by employers)
    @POST
    @RolesAllowed("EMPLOYER")
    public Response createContract(CreateContractDto dto) throws AppException {
        Contract createdContract = contractService.createContract(dto);
        return Response.status(Response.Status.CREATED).entity(ResponseModel.builder().data(createdContract).build()).build();
    }

    @GET
    @RolesAllowed({"EMPLOYER", "FREELANCER"})
    public Response getContracts(@QueryParam("jobId") Long jobId) throws AppException {
        if (jobId != null) {
            return Response.ok(ResponseModel.builder().data(contractService.getContractsByJob(jobId)).build()).build();
        }
        return Response.ok(ResponseModel.builder().data(contractService.getAllContracts()).build()).build();
    }

    // Retrieve a contract by its ID.
    @GET
    @Path("/{id}")
    public Response getContract(@PathParam("id") Long id) throws AppException {
        return Response.ok(ResponseModel.builder().data(contractService.getContractById(id)).build()).build();
    }

    // Endpoint for either party to update their acceptance on the contract.
    @PUT
    @Path("/{id}/status")
    public Response updateStatus(@PathParam("id") Long id, @QueryParam("isAccepted") boolean isAccepted) throws AppException {
        Contract updatedContract = contractService.updateContractStatus(id, isAccepted);
        return Response.ok(ResponseModel.builder().data(updatedContract).build()).build();
    }

    // Update overall contract status (e.g., "active", "completed", "cancelled")
    @PUT
    @Path("/{id}/status")
    public Response updateContractStatus(@PathParam("id") Long id, ContractStatusUpdateDto statusDto) throws AppException {
        Contract updatedContract = contractService.updateContractStatus(id, statusDto.getStatus());
        return Response.ok(ResponseModel.builder().data(updatedContract).build()).build();
    }
}
