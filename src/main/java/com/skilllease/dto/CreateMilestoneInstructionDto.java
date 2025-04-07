package com.skilllease.dto;

import com.skilllease.entities.MilestoneSubmissionType;

import java.time.LocalDateTime;

public record CreateMilestoneInstructionDto(
        Long contractId,
        String title,
        String description,
        LocalDateTime dueDate,
        boolean isFinal,
        MilestoneSubmissionType submissionType,
        String checklist
) {
}
