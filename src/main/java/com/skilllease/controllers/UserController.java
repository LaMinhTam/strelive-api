package com.skilllease.controllers;

import com.skilllease.services.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;

@Path("users")
@ApplicationScoped
public class UserController {
    @Inject
    private UserService userService;

}
