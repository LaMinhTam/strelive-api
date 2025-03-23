package com.skilllease.controllers;

import com.skilllease.dto.ContractAcceptDto;
import com.skilllease.dto.ContractStatusUpdateDto;
import com.skilllease.dto.CreateContractDto;
import com.skilllease.entities.Contract;
import com.skilllease.exception.AppException;
import com.skilllease.services.ContractService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

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
        return Response.status(Response.Status.CREATED).entity(createdContract).build();
    }

    // Retrieve a contract by its ID.
    @GET
    @Path("/{id}")
    public Response getContract(@PathParam("id") Long id) {
        Optional<Contract> contractOpt = contractService.getContractById(id);
        return contractOpt.map(Response::ok)
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    // Endpoint for either party to update their acceptance on the contract.
    @PUT
    @Path("/{id}/accept")
    public Response acceptContract(@PathParam("id") Long id, ContractAcceptDto acceptDto) throws AppException {
        Contract updatedContract = contractService.acceptContract(id, acceptDto);
        return Response.ok(updatedContract).build();
    }

    // Update overall contract status (e.g., "active", "completed", "cancelled")
    @PUT
    @Path("/{id}/status")
    public Response updateContractStatus(@PathParam("id") Long id, ContractStatusUpdateDto statusDto) throws AppException {
        Contract updatedContract = contractService.updateContractStatus(id, statusDto.getStatus());
        return Response.ok(updatedContract).build();
    }

    @PUT
    @Path("/{id}/finalize")
    @RolesAllowed("EMPLOYER")
    public Response finalizeContract(@PathParam("id") Long id) throws AppException {
        Contract finalizedContract = contractService.finalizeContract(id);
        return Response.ok(finalizedContract).build();
    }
}
