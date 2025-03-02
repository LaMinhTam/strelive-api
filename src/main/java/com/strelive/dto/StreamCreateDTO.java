package com.strelive.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record StreamCreateDTO(
    String title,
    String description
) {}