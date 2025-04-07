package com.skilllease.dto;

import com.skilllease.entities.Job;
import com.skilllease.entities.JobBid;
import com.skilllease.entities.Contract;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobDetailDTO {
    private Job job;
    private List<JobBid> bids;
    private Contract contract;
}
