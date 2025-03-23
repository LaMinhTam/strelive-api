package com.skilllease.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The contract for which the review is given.
    @ManyToOne(optional = false)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    // The user who gives the review.
    @ManyToOne(optional = false)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    // The user who is being reviewed.
    @ManyToOne(optional = false)
    @JoinColumn(name = "reviewee_id", nullable = false)
    private User reviewee;

    // Numeric rating (for example, 1 to 5)
    @Column(nullable = false)
    private Integer rating;

    // Optional text comment for additional feedback.
    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
