package com.skilllease.entities;

import com.skilllease.services.FreelancerService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "contracts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
    @ManyToOne
    @JoinColumn(name = "job_bid_id")
    private JobBid jobBid;

    @Column(name = "contract_start_date", nullable = false)
    private LocalDateTime contractStartDate;

    @Column(name = "contract_end_date")
    private LocalDateTime contractEndDate;

    @Column(name = "commitment_period")
    private Integer commitmentPeriod; // in months

    @Column(name = "support_availability")
    private String supportAvailability; // e.g., "24/7 support"

    @Column(name = "additional_policy", columnDefinition = "TEXT")
    private String additionalPolicy;

    @Column(name = "deposit_amount", nullable = false)
    private BigDecimal depositAmount;

    @Column(name = "final_payment_amount", nullable = false)
    private BigDecimal finalPaymentAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "deposit_status", nullable = false)
    private DepositStatus depositStatus; // "pending", "paid"

    @Enumerated(EnumType.STRING)
    @Column(name = "final_payment_status", nullable = false)
    private PaymentStatus finalPaymentStatus; // "pending", "paid"

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "employer_accepted", nullable = false)
    private Boolean employerAccepted = false;

    @Column(name = "freelancer_accepted", nullable = false)
    private Boolean freelancerAccepted = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ContractStatus status; // e.g., "draft", "negotiation", "active", "completed", "cancelled"
}
