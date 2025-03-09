package com.skilllease.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobCreateDto {
    private String jobTitle;
    private String jobDescription;
    private BigDecimal budget;
    private LocalDate deadline;
}
