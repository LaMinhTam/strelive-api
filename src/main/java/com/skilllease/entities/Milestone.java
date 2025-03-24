package com.skilllease.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    // The contract to which this milestone belongs.
    @ManyToOne(optional = false)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // Optional due date for the milestone.
    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // URL for an uploaded deliverable (file URL or external link).
    @Column(name = "deliverable_url")
    private String deliverableUrl;

    // The type of submission: FILE, LINK, or PREVIEW.
    @Enumerated(EnumType.STRING)
    @Column(name = "submission_type", nullable = false)
    private MilestoneSubmissionType submissionType;

    // Review status by the employer: e.g., "pending", "approved", "rejected".
    @Enumerated(EnumType.STRING)
    @Column(name = "review_status", nullable = false)
    private MilestoneStatus reviewStatus;

    // Optional feedback from the employer regarding the deliverable.
    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "final_milestone", nullable = false)
    private Boolean finalMilestone = false;

    // The final milestone will be hidden until the payment is done
    @Column(name = "hidden", nullable = false)
    private Boolean hidden = false;

    // New field: Freelancer's report on what was done, what might be missing, etc.
    @Column(name = "fulfillment_comment", columnDefinition = "TEXT")
    private String fulfillmentComment;

}
