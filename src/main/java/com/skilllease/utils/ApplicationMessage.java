package com.skilllease.utils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationMessage {
    public static final String UNAUTHORIZED = "Unauthorized";
    public static final String FORBIDDEN = "Forbidden";
    public static final String INTERNAL_SEVER_ERROR= "Internal sever error";
    public static final String BAD_REQUEST_ERROR= "Bad request error";
    public static final String MISSING_TOKEN_ERROR = "Missing token";
}
