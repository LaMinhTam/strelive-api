package com.skilllease.dto;

public record ReviewRequestDTO(
        Long contractId,
        Long revieweeId,
        Integer rating,
        String comment
) {
}
