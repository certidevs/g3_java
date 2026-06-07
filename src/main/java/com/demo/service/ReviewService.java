package com.demo.service;

import com.demo.model.Review;
import com.demo.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }

    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    public void delete(Review review) {
        reviewRepository.delete(review);
    }

    public List<Review> findByHouseIdOrderByCreatedAtDesc(Long houseId) {
        return reviewRepository.findByHouse_IdOrderByCreatedAtDesc(houseId);
    }

    public List<Review> findTop5ByOrderByRatingAsc() {
        return reviewRepository.findTop5ByOrderByRatingAsc();
    }

    public Double getAverageRating(Long houseId) {
        Double avg = reviewRepository.getAverageRatingForHouse(houseId);
        return avg != null ? avg : 0.0;
    }
}
