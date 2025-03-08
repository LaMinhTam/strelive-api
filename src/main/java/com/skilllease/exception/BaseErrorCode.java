package com.skilllease.exception;

import jakarta.ws.rs.core.Response;

import java.io.Serializable;

public interface BaseErrorCode extends Serializable {
    String getMessage();
    Response.Status getStatusCode();
}