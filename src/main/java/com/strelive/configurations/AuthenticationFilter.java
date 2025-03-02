package com.strelive.configurations;

import com.strelive.entities.User;
import com.strelive.exception.UnauthorizedException;
import com.strelive.utils.ApplicationMessage;
import com.strelive.utils.DecodeToken;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import jakarta.annotation.Priority;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
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
    public void filter(ContainerRequestContext reqCtx) throws IOException {
        Method method = resourceInfo.getResourceMethod();
        if (method.isAnnotationPresent(RolesAllowed.class)) {
            String token = getTokenFromHeader(reqCtx);
            RolesAllowed rolesAllowed = method.getAnnotation(RolesAllowed.class);
            checkAccess(DecodeToken.getRolesTokenArray(token), Arrays.asList(rolesAllowed.value()));
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