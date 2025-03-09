package com.skilllease.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceCreateDto {
    private String title;
    private String description;
    private BigDecimal price;
    private Boolean depositRequired;
    private Integer depositPercentage;
    private Long categoryId;
}
