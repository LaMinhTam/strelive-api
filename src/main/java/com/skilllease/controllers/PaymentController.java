package com.skilllease.controllers;

import com.skilllease.dto.PaymentResponse;
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
        return Response.ok(response).build();
    }

    @GET
    @Path("/vn-pay-callback{extra: (/.*)?}")
    public Response payCallbackHandler(@Context HttpServletRequest request) throws AppException {
        paymentService.handleVnPayCallback(request);
        String status = request.getParameter("vnp_ResponseCode");
        URI redirectUri;
        if ("00".equals(status)) {
            redirectUri = UriBuilder.fromUri("http://localhost:3000").build();
        } else {
            redirectUri = UriBuilder.fromUri("http://localhost:3000/error").build();
        }
        return Response.seeOther(redirectUri).build();
    }

    @GET
    @Path("/final-pay/{milestoneId}")
    public Response finalPay(@Context HttpServletRequest request, @PathParam("milestoneId") Long milestoneId) throws AppException {
        PaymentResponse response = paymentService.createFinalPayment(request, milestoneId);
        return Response.ok(response).build();
    }
}
