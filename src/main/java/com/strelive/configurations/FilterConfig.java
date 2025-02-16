package com.strelive.configurations;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

@ApplicationScoped
public class FilterConfig {

    @Produces
    @Priority(1)
    public FilterRegistration.Dynamic jwtTokenFilter(ServletContext servletContext) throws ServletException {
        FilterRegistration.Dynamic registration = servletContext.addFilter("jwtTokenFilter", (Filter) new JwtTokenFilter());
        registration.addMappingForUrlPatterns(null, false, "/*");
        return registration;
    }
}
