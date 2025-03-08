package com.skilllease.controllers;

import com.skilllease.services.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;

@Path("users")
public class UserController {
    @Inject
    private UserService userService;

}
