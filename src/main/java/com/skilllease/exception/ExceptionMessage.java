package com.skilllease.exception;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionMessage {
    public static final String UNAUTHORIZED = "Unauthorized";
    public static final String FORBIDDEN = "Forbidden";
    public static final String INTERNAL_SEVER_ERROR = "Internal sever error";
    public static final String BAD_REQUEST_ERROR = "Bad request error";
    public static final String MISSING_TOKEN = "Missing token";
    public static final String INVALID_TOKEN = "Invalid token";
    public static final String INVALID_OR_EXPIRED_TOKEN = "Invalid or expired token";
    public static final String INTERNAL_SERVER_ERROR = "Internal server error";
    public static final String NOT_FOUND = "Not found";
    public static final String INVALID_SECURED_PATH_PARAM = "Invalid secured path param";
    public static final String ATTEMPT_ERROR = "Too many failed attempts";
    public static final String JOB_NOT_FOUND = "Job not found";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String JOB_BID_NOT_FOUND = "Job bid not found";
    public static final String JOB_BID_CANNOT_BE_DELETED = "Job bid cannot be deleted";
    public static final String CONTRACT_NOT_FOUND = "Contract not found";
    public static final String INVALID_HASH_DATA = "Invalid hash data";
    public static final String WALLET_NOT_FOUND = "Wallet not found";
    public static final String CONTRACT_NOT_COMPLETED = "Contract must be completed before reviews can be submitted.";
    public static final String SELF_REVIEW = "Self review is not allowed";
    public static final String PAYMENT_PENDING = "Final payment is pending.";
    public static final String MILESTONE_NOT_FOUND = "Milestone not found";
    public static final String FINAL_MILESTONE_NOT_SUBMITTED = "Final milestone not submitted.";
    public static final String MILESTONE_NOT_APPROVED = "Milestone not approved.";
}
