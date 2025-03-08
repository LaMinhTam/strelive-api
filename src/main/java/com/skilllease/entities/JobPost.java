package com.skilllease.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_posts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Reference to the employer who posted the job
    @ManyToOne(optional = false)
    @JoinColumn(name = "employer_id", nullable = false)
    private User employer;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(name = "job_description", columnDefinition = "TEXT")
    private String jobDescription;

    @Column(nullable = false)
    private BigDecimal budget;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    private LocalDate deadline;
}
