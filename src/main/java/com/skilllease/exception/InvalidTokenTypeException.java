package com.skilllease.exception;

import com.skilllease.dto.ResponseModel;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class InvalidTokenTypeException extends WebApplicationException {
    public InvalidTokenTypeException(String message) {
        super(Response.status(Response.Status.UNAUTHORIZED)
                .entity(ResponseModel.builder().message(message).build())
                .build());
    }
}
