package com.skilllease.dto;

import java.time.LocalDate;

public record UpdateMilestoneDto(
        String title,
        String description,
        LocalDate dueDate,
        String checklist) {
}
