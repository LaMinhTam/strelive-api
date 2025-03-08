package com.skilllease.exception;

import com.skilllease.dto.ResponseModel;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class InvalidTokenTypeException extends AppException {
    public InvalidTokenTypeException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
