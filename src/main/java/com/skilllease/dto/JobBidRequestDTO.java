package com.skilllease.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobBidRequestDTO {
    private Long jobId;
    private BigDecimal bidAmount;
    private Integer commitmentDays;
    private String message;
}
