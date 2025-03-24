package com.skilllease.dto;

import jakarta.ws.rs.FormParam;
import lombok.Data;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import java.io.InputStream;

@Data
public class FulfillMilestoneFileForm {
    // Optionally, you can include milestoneId in the form or use the path parameter.
    @FormParam("milestoneId")
    @PartType("text/plain")
    private Long milestoneId;

    @FormParam("fulfillmentComment")
    @PartType("text/plain")
    private String fulfillmentComment;

    // The uploaded file.
    @FormParam("file")
    @PartType("application/octet-stream")
    private InputStream file;

    // The original file name.
    @FormParam("fileName")
    @PartType("text/plain")
    private String fileName;
}
