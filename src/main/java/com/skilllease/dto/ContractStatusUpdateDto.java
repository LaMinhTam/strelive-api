package com.skilllease.dto;

import com.skilllease.entities.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractStatusUpdateDto {
    private ContractStatus status;
}
