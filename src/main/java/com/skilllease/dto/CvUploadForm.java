package com.skilllease.dto;

import jakarta.ws.rs.FormParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jboss.resteasy.annotations.providers.multipart.PartType;
import java.io.InputStream;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CvUploadForm {

    @FormParam("file")
    @PartType("application/octet-stream")
    private InputStream file;

    @FormParam("fileName")
    @PartType("text/plain")
    private String fileName;
}
