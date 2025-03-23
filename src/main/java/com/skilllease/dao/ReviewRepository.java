package com.skilllease.dao;

import com.skilllease.entities.Review;
import jakarta.data.repository.*;

import java.util.List;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE r.reviewee.id = :userId")
    List<Review> findByRevieweeId(@Param("userId") Long userId);

    @Query("SELECT r FROM Review r WHERE r.contract.id = :contractId")
    List<Review> findByContractId(@Param("contractId") Long contractId);

    @Insert
    Review save(Review review);

    @Update
    Review update(Review review);
}
