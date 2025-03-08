package com.skilllease.exception;

public class IntermediateException extends RuntimeException {
    public IntermediateException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
