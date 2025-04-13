package com.skilllease.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobBidRequestDTO {
    private Long jobId;
    private BigDecimal bidAmount;
    private String message;
    private LocalDate proposedStartDate;
    private LocalDate proposedEndDate;
    private String additionalPolicy;
}
