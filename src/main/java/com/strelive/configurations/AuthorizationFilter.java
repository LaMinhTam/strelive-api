package com.strelive.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strelive.dto.ResponseModel;
import com.strelive.exception.TokenInvalidException;
import com.strelive.utils.ApplicationMessage;
import com.strelive.utils.ConfigUtils;
import com.strelive.utils.DecodeToken;
import jakarta.annotation.Priority;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.core.HttpHeaders;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@WebFilter("/*")
@Priority(1)
public class AuthorizationFilter implements Filter {
    private static final String CONTEXT_PATH = ConfigUtils.getProperty("context.path");
    private static final String ROLE_USER = ConfigUtils.getProperty("roles.user");
    private static final Set<String> WHITELIST = Arrays.stream(ConfigUtils.getProperty("whitelist.urls").split(","))
            .map(origin -> CONTEXT_PATH + origin )
            .collect(Collectors.toSet());

    private static final Map<String, Map<String, List<String>>> URL_ROLE_MAPPING = Map.of(
            CONTEXT_PATH + "/api/users", Map.of(
                    HttpMethod.POST, List.of(ROLE_USER)
            )
    );

    private static final Map<String, List<String>> PUBLIC_URL_MAPPING = Map.of(
            CONTEXT_PATH + "/api/auth", List.of(HttpMethod.GET, HttpMethod.POST),
            CONTEXT_PATH + "/api/roles", List.of(HttpMethod.GET, HttpMethod.POST),
            CONTEXT_PATH + "/api/stream/auth", List.of(HttpMethod.POST),
            CONTEXT_PATH + "/api/stream", List.of(HttpMethod.GET)
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String token = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String requestURL = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        if (isPreflightRequest(httpRequest) || isWhitelisted(requestURL) || isPublicRoute(requestURL, method)) {
            chain.doFilter(request, response);
            return;
        }

        if (token == null) {
            sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, ApplicationMessage.UNAUTHORIZED);
            return;
        }

        try {
            String role = validateToken(token);
            if (!isAuthorized(requestURL, method, role)) {
                sendErrorResponse(httpResponse, HttpServletResponse.SC_FORBIDDEN, ApplicationMessage.FORBIDDEN);
                return;
            }
            chain.doFilter(request, response);
        } catch (TokenInvalidException e) {
            sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }

    private boolean isPreflightRequest(HttpServletRequest request) {
        return "OPTIONS".equalsIgnoreCase(request.getMethod());
    }

    private boolean isWhitelisted(String url) {
        return WHITELIST.stream().anyMatch(url::startsWith) || url.startsWith(CONTEXT_PATH + "/api/stream");
    }

    private String validateToken(String token) throws TokenInvalidException {
        String role = DecodeToken.getRoleToken(token);
        long expirationTime = DecodeToken.getExpirationToken(token);
        if (expirationTime < new Date().getTime()) {
            throw new TokenInvalidException(ApplicationMessage.UNAUTHORIZED);
        }
        return role;
    }

    private boolean isAuthorized(String url, String method, String role) {
        return URL_ROLE_MAPPING.entrySet().stream()
                .filter(entry -> url.matches(entry.getKey()))
                .findFirst()
                .map(entry -> entry.getValue().getOrDefault(method, List.of()).contains(role))
                .orElse(true);
    }

    private boolean isPublicRoute(String url, String method) {
        return PUBLIC_URL_MAPPING.entrySet().stream()
                .anyMatch(entry -> url.startsWith(entry.getKey()) && entry.getValue().contains(method));
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        ResponseModel<Object> responseModel = ResponseModel.builder().message(message).build();
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseModel));
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Do nothing because we don't need to initialize anything
        // This method is required by the Filter interface
    }

    @Override
    public void destroy() {
        // Do nothing because we don't need to destroy anything
        // This method is required by the Filter interface
    }
}
