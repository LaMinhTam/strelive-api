package com.skilllease.dto;

import com.skilllease.entities.MilestoneStatus;

public record MilestoneReviewDto(MilestoneStatus reviewStatus, String feedback) {
}
