package com.strelive.configurations;

import jakarta.servlet.FilterConfig;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class JwtTokenFilter implements Filter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String TOKEN_ATTRIBUTE = "currentToken";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String token = httpRequest.getHeader(AUTH_HEADER);
        if (token != null && token.startsWith("Bearer ")) {
            httpRequest.setAttribute(TOKEN_ATTRIBUTE, token);
        } else {
            httpRequest.setAttribute(TOKEN_ATTRIBUTE, null);
        }
        chain.doFilter(request, response);
    }

    public static String getCurrentToken(HttpServletRequest request) {
        return request.getHeader(AUTH_HEADER);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Do nothing because we don't need to initialize anything
        // This method is required to implement the Filter interface
    }

    @Override
    public void destroy() {
        // Do nothing because we don't need to destroy anything
        // This method is required to implement the Filter interface
    }
}