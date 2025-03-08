package com.skilllease.exception;

public class EntityNotFoundException extends AppException {

    public EntityNotFoundException(BaseErrorCode errorCode) {
        super(errorCode);
    }

}