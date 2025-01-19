package com.strelive.jakarta.hello;

import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.ApplicationPath;

@ApplicationPath("rest")
public class Strelive extends Application {
  // Needed to enable Jakarta REST and specify path.    
}
