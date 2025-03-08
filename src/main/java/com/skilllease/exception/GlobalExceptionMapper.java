package com.skilllease.exception;

import com.skilllease.dto.ResponseModel;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionMapper.class);

    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof AppException appexception) {
            BaseErrorCode errorCode = appexception.getErrorCode();
            logger.error("AppException: {}", errorCode.getMessage());

            return Response.status(errorCode.getStatusCode())
                    .entity(ResponseModel.builder().message(errorCode.getMessage()).build())
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } else if (exception instanceof WebApplicationException webEx) {
            logger.error("WebApplicationException: {}", exception.getMessage());
            return Response.status(webEx.getResponse().getStatus())
                    .entity(ResponseModel.builder().message(webEx.getMessage()).build())
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        return Response.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatusCode())
                .entity(ResponseModel.builder().message(exception.getMessage()).build())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
