package com.skilllease.dto;

public record FulfillMilestoneDto(
        String deliverableUrl,   // The URL to the submitted file or link.
        String fulfillmentComment,
        String checklist
) {
}
