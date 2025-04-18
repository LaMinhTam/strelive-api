package com.skilllease.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobDto {
    private Long id;
    private String jobTitle;
    private String jobDescription;
    private BigDecimal budget;
    private LocalDateTime createdAt;
    private LocalDate deadline;
    private UserDto employer;
    private CategoryDto category;
    private List<MilestoneDto> milestones;
}