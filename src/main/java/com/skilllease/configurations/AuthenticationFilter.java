package com.skilllease.configurations;

import com.skilllease.exception.ErrorCode;
import com.skilllease.exception.IntermediateException;
import com.skilllease.exception.TokenInvalidException;
import com.skilllease.exception.UnauthorizedException;
import com.skilllease.utils.ApplicationMessage;
import com.skilllease.utils.DecodeToken;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

import jakarta.annotation.Priority;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext reqCtx) {
        Method method = resourceInfo.getResourceMethod();
        if (method.isAnnotationPresent(RolesAllowed.class)) {
            String token = getTokenFromHeader(reqCtx);
            RolesAllowed rolesAllowed = method.getAnnotation(RolesAllowed.class);
            try {
                checkAccess(DecodeToken.getRolesTokenArray(token), Arrays.asList(rolesAllowed.value()));
            } catch (TokenInvalidException e) {
                throw new IntermediateException(ErrorCode.UNAUTHORIZED);
            }
        }
    }

    private String getTokenFromHeader(ContainerRequestContext reqCtx) {
        String authHeader = reqCtx.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            throw new UnauthorizedException(ApplicationMessage.MISSING_TOKEN_ERROR);
        }
        return authHeader;
    }

    private void checkAccess(List<String> userRole, List<String> allowedRoles) {
        if (allowedRoles.contains("*")) {
            return;
        }
        if (userRole.stream().noneMatch(allowedRoles::contains)) {
            throw new ForbiddenException(ApplicationMessage.FORBIDDEN);
        }
    }
}