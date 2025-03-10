package com.skilllease.dto;

import com.skilllease.entities.Service;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDto {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private Boolean depositRequired;
    private Integer depositPercentage;
    private CategoryDto category;
}
