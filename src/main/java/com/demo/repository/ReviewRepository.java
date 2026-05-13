package com.demo.repository;

import com.demo.model.Review;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

//    List<Review> findAllByActiveTrue();
//
//    List<Review> findByHouseIdOrderByCreationDateDesc(Long houseId);
//
//    List<Review> findByHouseIdAndRatingOrderByCreationDateDesc(Long houseId, Integer rating);

//    // TODO:
    // - Considerar filtrar por rango between, por ejemplo de fechas o de rating, ordenadas por creationDate desc

    List<Review> findAllByActiveTrue();
    // List<Review> findAllByActiveFalse();

    List<Review> findByHouse_IdOrderByCreatedAtDesc(Long id);

//    List<Review> findByHouseIdOrderByCreationDateDesc(Long houseId);

    List<Review> findByHouse_IdAndRatingOrderByCreatedAtDesc(Long id, Integer rating);
//    List<Review> findByHouseIdAndRatingOrderByCreationDateDesc(Long houseId, Integer rating);
}