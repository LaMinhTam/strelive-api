package com.strelive;

import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.ApplicationPath;

@ApplicationPath("api")
public class Strelive extends Application {
    // Needed to enable Jakarta REST and specify path.
}
