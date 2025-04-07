package com.skilllease.dto;

import java.time.LocalDateTime;

public record UpdateMilestoneDto(
        String title,
        String description,
        LocalDateTime dueDate,
        String checklist) {
}
