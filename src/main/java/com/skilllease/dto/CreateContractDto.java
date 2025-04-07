package com.skilllease.dto;

import com.skilllease.entities.ContractType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateContractDto {
    private ContractType contractType; // "direct" or "bid"
    private Long serviceId;      // Optional: for direct engagements
    private Long jobBidId;    // Optional: for bid engagements
    private LocalDateTime contractStartDate;
    private LocalDateTime contractEndDate;
    private String supportAvailability; // e.g., "24/7 support"
    
    // The field for additional policy input:
    private String additionalPolicy;
    
    private BigDecimal depositAmount;
    private BigDecimal finalPaymentAmount;
}
