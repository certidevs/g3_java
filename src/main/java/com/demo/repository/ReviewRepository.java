package com.demo.repository;

import com.demo.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

//    List<Review> findAllByActiveTrue();
//
//    List<Review> findByHouseIdOrderByCreationDateDesc(Long houseId);
//
//    List<Review> findByHouseIdAndRatingOrderByCreationDateDesc(Long houseId, Integer rating);

    // TODO:
    // - Considerar filtrar por rango between, por ejemplo de fechas o de rating, ordenadas por creationDate desc


    Optional<Review> findByIdAndActiveTrue(Long id);

    List<Review> findAllByActiveTrue();
    // List<Review> findAllByActiveFalse();

    List<Review> findByHouse_IdOrderByCreatedAtDesc(Long houseId);

//    List<Review> findByHouseIdOrderByCreationDateDesc(Long houseId);

    List<Review> findByHouse_IdAndRatingOrderByCreatedAtDesc(Long id, Integer rating);
//    List<Review> findByHouseIdAndRatingOrderByCreationDateDesc(Long houseId, Integer rating);
}