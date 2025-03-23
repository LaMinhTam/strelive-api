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
    INVALID_SECURED_PATH_PARAM(ExceptionMessage.INVALID_SECURED_PATH_PARAM, Response.Status.BAD_REQUEST),
    JOB_NOT_FOUND(ExceptionMessage.JOB_NOT_FOUND, Response.Status.NOT_FOUND),
    USER_NOT_FOUND(ExceptionMessage.USER_NOT_FOUND, Response.Status.NOT_FOUND),
    JOB_BID_NOT_FOUND(ExceptionMessage.JOB_BID_NOT_FOUND, Response.Status.NOT_FOUND),
    JOB_BID_CANNOT_BE_DELETED(ExceptionMessage.JOB_BID_CANNOT_BE_DELETED, Response.Status.BAD_REQUEST),
    CONTRACT_NOT_FOUND(ExceptionMessage.CONTRACT_NOT_FOUND, Response.Status.NOT_FOUND),
    INVALID_HASH_DATA(ExceptionMessage.INVALID_HASH_DATA, Response.Status.BAD_REQUEST),
    WALLET_NOT_FOUND(ExceptionMessage.WALLET_NOT_FOUND, Response.Status.BAD_REQUEST),
    CONTRACT_NOT_COMPLETED(ExceptionMessage.CONTRACT_NOT_COMPLETED, Response.Status.BAD_REQUEST),
    SELF_REVIEW(ExceptionMessage.SELF_REVIEW, Response.Status.BAD_REQUEST),
    PAYMENT_PENDING(ExceptionMessage.PAYMENT_PENDING, Response.Status.BAD_REQUEST),
    MILESTONE_NOT_FOUND(ExceptionMessage.MILESTONE_NOT_FOUND, Response.Status.NOT_FOUND),
    FINAL_MILESTONE_NOT_SUBMITTED(ExceptionMessage.FINAL_MILESTONE_NOT_SUBMITTED, Response.Status.BAD_REQUEST),
    MILESTONE_NOT_APPROVED(ExceptionMessage.MILESTONE_NOT_APPROVED, Response.Status.BAD_REQUEST);

    private final String message;
    private final Response.Status statusCode;

    ErrorCode(String message, Response.Status statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

}
