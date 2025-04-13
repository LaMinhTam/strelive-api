package com.skilllease.dto;

import com.skilllease.entities.ContractType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CreateContractDto {
    ContractType contractType;
    Long serviceId;
    Long jobBidId;
    LocalDate contractStartDate;
    LocalDate contractEndDate;
    String additionalPolicy;
}
