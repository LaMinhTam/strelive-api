package com.skilllease.dto;

public record FulfillMilestoneDto(
        String deliverableUrl,
        String fulfillmentComment,
        String checklist
) {
}
