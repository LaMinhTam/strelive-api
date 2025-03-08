package com.skilllease.exception;

public class TokenInvalidException extends AppException {
    public TokenInvalidException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
