package com.skilllease.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractAcceptDto {
    private Boolean employerAccepted;
    private Boolean freelancerAccepted;
}
