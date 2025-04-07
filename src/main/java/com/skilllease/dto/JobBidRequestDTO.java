package com.skilllease.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobBidRequestDTO {
    private Long jobId;
    private BigDecimal bidAmount;
    private String message;
    private LocalDateTime proposedStartDate;
    private LocalDateTime proposedEndDate;
    private String supportAvailability;
    private String additionalPolicy;
    private BigDecimal depositAmount;
    private BigDecimal finalPaymentAmount;
}
