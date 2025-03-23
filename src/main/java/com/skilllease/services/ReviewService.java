package com.skilllease.services;

import com.skilllease.dto.ReviewRequestDTO;
import com.skilllease.entities.Review;

import java.util.List;

public interface ReviewService {
    Review createReview(ReviewRequestDTO reviewRequestDTO) throws Exception;
    List<Review> getReviewsByUser(Long userId);
    List<Review> getReviewsByContract(Long contractId);
}
