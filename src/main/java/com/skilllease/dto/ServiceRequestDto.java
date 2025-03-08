package com.skilllease.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ServiceRequestDto {
    private String title;
    private String description;
    private BigDecimal price;
    private Boolean depositRequired;
    private Integer depositPercentage;
    private Integer categoryId;
}
