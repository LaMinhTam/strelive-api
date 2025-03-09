package com.skilllease.utils;

import com.skilllease.entities.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import static com.skilllease.utils.AuthUtils.getCurrentUser;

@RequestScoped
public class AuthService {
    @Inject
    private HttpServletRequest request;

    public User getCurrentEmployer() {
        return getCurrentUser(request);
    }
}
