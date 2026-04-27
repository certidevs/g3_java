package com.demo.repository;

import com.demo.model.Review;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // TODO:
    // - Considerar filtrar por rango between, por ejemplo de fechas o de rating, ordenadas por creationDate desc

    List<Review> findByHouse_IdOrderByCreatedAtDesc(Long id);

    List<com.demo.model.Review> findByHouse_IdAndRatingOrderByCreatedAtDesc(Long id, Integer rating);
}