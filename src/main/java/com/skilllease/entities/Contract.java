package com.skilllease.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "contracts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The employer who initiates the contract
    @ManyToOne(optional = false)
    @JoinColumn(name = "employer_id", nullable = false)
    private User employer;

    // The freelancer who is hired
    @ManyToOne(optional = false)
    @JoinColumn(name = "freelancer_id", nullable = false)
    private User freelancer;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_type", nullable = false)
    private ContractType contractType; // "direct" or "bid"

    // For direct engagements (from the SERVICES table)
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    // For bid engagements (from the JOB_BIDS table)
    @OneToOne
    @JoinColumn(name = "job_bid_id")
    @JsonIgnore
    private JobBid jobBid;

    @Column(name = "contract_start_date", nullable = false)
    private LocalDateTime contractStartDate;

    @Column(name = "contract_end_date")
    private LocalDateTime contractEndDate;

    @Column(name = "additional_policy", columnDefinition = "TEXT")
    private String additionalPolicy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "employer_accepted")
    private Boolean employerAccepted;

    @Column(name = "freelancer_accepted")
    private Boolean freelancerAccepted;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ContractStatus status; // e.g., "negotiation", "active", "completed", "cancelled"
}
