package com.skilllease.exception;

import jakarta.ws.rs.core.Response;
import lombok.Getter;

@Getter
public enum ErrorCode implements BaseErrorCode {

    INTERNAL_SERVER_ERROR(ExceptionMessage.INTERNAL_SERVER_ERROR, Response.Status.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED(ExceptionMessage.UNAUTHORIZED, Response.Status.UNAUTHORIZED),
    FORBIDDEN(ExceptionMessage.FORBIDDEN, Response.Status.FORBIDDEN),
    NOT_FOUND(ExceptionMessage.NOT_FOUND, Response.Status.NOT_FOUND),
    ATTEMPT_ERROR(ExceptionMessage.ATTEMPT_ERROR, Response.Status.TOO_MANY_REQUESTS),
    INVALID_SECURED_PATH_PARAM(ExceptionMessage.INVALID_SECURED_PATH_PARAM, Response.Status.BAD_REQUEST);

    private final String message;
    private final Response.Status statusCode;

    ErrorCode(String message, Response.Status statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

}
