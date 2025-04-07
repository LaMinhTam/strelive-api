package com.skilllease.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_bids")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobBid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "job_post_id", nullable = false)
    private Job job;

    @ManyToOne(optional = false)
    @JoinColumn(name = "freelancer_id", nullable = false)
    private User freelancer;

    @Column(name = "bid_amount", nullable = false)
    private BigDecimal bidAmount;

    @Column(name = "deposit_amount", nullable = false)
    private BigDecimal depositAmount;

    @Column(name = "final_payment_amount", nullable = false)
    private BigDecimal finalPaymentAmount;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String status; // "pending", "accepted", "rejected"

    @Column(name = "proposed_start_date")
    private LocalDateTime proposedStartDate;

    @Column(name = "proposed_end_date")
    private LocalDateTime proposedEndDate;

    @Column(name = "support_availability")
    private String supportAvailability;

    @Column(name = "additional_policy", columnDefinition = "TEXT")
    private String additionalPolicy;

    @OneToOne(mappedBy = "jobBid", fetch = FetchType.LAZY)
    @JsonIgnore
    private Contract contract;

}
