package com.skilllease.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDto {
    private String title;
    private String description;
    private BigDecimal price;
    private Boolean depositRequired;
    private Integer depositPercentage;
    private Long categoryId;
}
