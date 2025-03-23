package com.skilllease.dto;

import lombok.Data;
import org.jboss.resteasy.annotations.jaxrs.FormParam;

import java.io.InputStream;

@Data
public class MilestoneFileForm {
    @FormParam("contractId")
    private Long contractId;

    @FormParam("title")
    private String title;

    @FormParam("description")
    private String description;

    @FormParam("dueDate")
    private String dueDate;

    @FormParam("file")
    private InputStream file;

    @FormParam
    private boolean isFinal;
}
