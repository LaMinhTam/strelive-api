package com.skilllease.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "milestones")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Milestone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "job_id", nullable = false)
    @JsonIgnore
    private Job job;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    // Optional due date for the milestone.
    @Column(name = "due_date")
    private LocalDate dueDate;

    // URL for an uploaded deliverable (file URL or external link).
    @Column(name = "deliverable_url")
    private String deliverableUrl;

    // The type of submission: FILE, LINK, or PREVIEW.
    @Enumerated(EnumType.STRING)
    @Column(name = "submission_type")
    private MilestoneSubmissionType submissionType;

    // Review status by the employer: e.g., "pending", "approved", "rejected".
    @Enumerated(EnumType.STRING)
    @Column(name = "review_status")
    private MilestoneStatus reviewStatus;

    // Optional feedback from the employer regarding the deliverable.
    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    // The final milestone will be hidden until the payment is done
    @Column(name = "hidden", nullable = false)
    private Boolean hidden = false;

    // New field: Freelancer's report on what was done, what might be missing, etc.
    @Column(name = "fulfillment_comment", columnDefinition = "TEXT")
    private String fulfillmentComment;

    @Column(name = "checklist", columnDefinition = "TEXT")
    private String checklist; // Markdown-formatted checklist content

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "effort", nullable = false)
    private int effort;
}
