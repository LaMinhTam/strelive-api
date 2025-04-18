package com.skilllease.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobCreateDto {
    private Long id;
    private String jobTitle;
    private Long categoryId;
    private String jobDescription;
    private BigDecimal budget;
    private LocalDate deadline;
    private List<MilestoneCreateDto> milestones;
}
