package com.skilllease.dto;

import com.skilllease.entities.MilestoneStatus;
import com.skilllease.entities.MilestoneSubmissionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MilestoneDto {
    private Long id;
    private Long jobId;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate dueDate;
    private String deliverableUrl;
    private MilestoneSubmissionType submissionType;
    private MilestoneStatus reviewStatus;
    private String feedback;
    private Boolean hidden;
    private String fulfillmentComment;
    private String checklist; // Markdown-formatted checklist content
    private BigDecimal amount;
}
