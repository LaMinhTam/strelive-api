package com.skilllease.dto;

import jakarta.ws.rs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;
import lombok.Data;

import java.io.InputStream;

@Data
public class ProfilePictureForm {
    @FormParam("file")
    @PartType("application/octet-stream")
    private InputStream file;

    @FormParam("fileName")
    @PartType("text/plain")
    private String fileName;
}
