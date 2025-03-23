package com.skilllease.services.impl;

import com.skilllease.dao.ReviewRepository;
import com.skilllease.dto.ReviewRequestDTO;
import com.skilllease.entities.Contract;
import com.skilllease.entities.ContractStatus;
import com.skilllease.entities.Review;
import com.skilllease.entities.User;
import com.skilllease.exception.AppException;
import com.skilllease.exception.ErrorCode;
import com.skilllease.services.ContractService;
import com.skilllease.services.ReviewService;
import com.skilllease.services.UserService;
import com.skilllease.utils.AuthService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class ReviewServiceImpl implements ReviewService {
    @Inject
    private ReviewRepository reviewRepository;

    @Inject
    private ContractService contractService;

    @Inject
    private UserService userService;

    @Inject
    private AuthService authService;

    @Override
    public Review createReview(ReviewRequestDTO reviewRequestDTO) throws Exception {
        Contract contract = contractService.getContractById(reviewRequestDTO.contractId())
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));
        
        // Check that the contract is completed before reviewing.
        if (!contract.getStatus().equals(ContractStatus.COMPLETED)) {
            throw new AppException(ErrorCode.CONTRACT_NOT_COMPLETED);
        }

        // Get the current user as the reviewer.
        User reviewer = authService.getCurrentUser();

        // Ensure that the reviewee exists.
        User reviewee = userService.getById(reviewRequestDTO.revieweeId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Optional: Validate that reviewer and reviewee are part of the contract.
        if (!reviewer.getId().equals(contract.getEmployer().getId()) && 
            !reviewer.getId().equals(contract.getFreelancer().getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        if (!reviewee.getId().equals(contract.getEmployer().getId()) &&
            !reviewee.getId().equals(contract.getFreelancer().getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        // Prevent self-review.
        if (reviewer.getId().equals(reviewee.getId())) {
            throw new AppException(ErrorCode.SELF_REVIEW);
        }
        
        // Optionally: Prevent duplicate reviews for the same contract by the same reviewer.
        // (This may require adding a custom query to ReviewRepository.)

        Review review = Review.builder()
                .contract(contract)
                .reviewer(reviewer)
                .reviewee(reviewee)
                .rating(reviewRequestDTO.rating())
                .comment(reviewRequestDTO.comment())
                .createdAt(LocalDateTime.now())
                .build();

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewsByUser(Long userId) {
        return reviewRepository.findByRevieweeId(userId);
    }

    @Override
    public List<Review> getReviewsByContract(Long contractId) {
        return reviewRepository.findByContractId(contractId);
    }
}
