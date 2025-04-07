package com.skilllease.controllers;

import com.skilllease.dto.PaymentResponse;
import com.skilllease.dto.ResponseModel;
import com.skilllease.exception.AppException;
import com.skilllease.services.impl.PaymentService;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import java.net.URI;

@Path("/payment")
@Produces(MediaType.APPLICATION_JSON)
public class PaymentController {
    @Inject
    private PaymentService paymentService;

    @GET
    @Path("/vn-pay")
    public Response pay(@Context HttpServletRequest request) throws AppException {
        PaymentResponse response = paymentService.createVnPayPayment(request);
        return Response.ok(ResponseModel.builder().data(response).build()).build();
    }

    @GET
    @Path("/vn-pay-callback{extra: (/.*)?}")
    public Response payCallbackHandler(@Context HttpServletRequest request) throws AppException {
        paymentService.handleVnPayCallback(request);
        String status = request.getParameter("vnp_ResponseCode");

        URI redirectUri;
        if ("00".equals(status)) {
            redirectUri = UriBuilder.fromUri("http://localhost:5173/").build();
        } else {
            redirectUri = UriBuilder.fromUri("http://localhost:5173/error").build();
        }
        return Response.seeOther(redirectUri).build();
    }

    @POST
    @Path("/contract/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response payContract(@PathParam("id") Long id) throws AppException {
        PaymentResponse response = paymentService.payForContract(id);
        return Response.ok(ResponseModel.builder().data(response).build()).build();
    }

    @POST
    @Path("/milestone/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response payMilestone(@PathParam("id") Long id) throws AppException {
        PaymentResponse response = paymentService.payForMilestone(id);
        return Response.ok(ResponseModel.builder().data(response).build()).build();
    }

    @GET
    @Path("/wallet")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserWallet() throws AppException {
        return Response.ok(ResponseModel.builder().data(paymentService.getUserWallet()).build()).build();
    }

}
