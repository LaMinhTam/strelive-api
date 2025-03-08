package com.skilllease.exception;

import lombok.Getter;

@Getter
public class AppException extends Exception {
    private final BaseErrorCode errorCode;

    public AppException(BaseErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
