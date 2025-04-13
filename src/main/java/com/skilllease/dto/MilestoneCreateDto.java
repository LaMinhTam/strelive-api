package com.skilllease.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.skilllease.entities.MilestoneStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MilestoneCreateDto {
    private Long id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate dueDate;
    private MilestoneStatus reviewStatus;
    private String checklist;
    private int effort;
}
