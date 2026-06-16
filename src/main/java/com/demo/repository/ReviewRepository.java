package com.demo.repository;

import com.demo.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

//    List<Review> findAllByActiveTrue();
//
//    List<Review> findByHouseIdOrderByCreationDateDesc(Long houseId);
//
//    List<Review> findByHouseIdAndRatingOrderByCreationDateDesc(Long houseId, Integer rating);

    // TODO:
    // - Considerar filtrar por rango between, por ejemplo de fechas o de rating, ordenadas por creationDate desc

    List<Review> findByHouse_IdOrderByCreatedAtDesc(Long houseId);

//    List<Review> findByHouseIdOrderByCreationDateDesc(Long houseId);

    List<Review> findByHouse_IdAndRatingOrderByCreatedAtDesc(Long id, Integer rating);
//    List<Review> findByHouseIdAndRatingOrderByCreationDateDesc(Long houseId, Integer rating);

    List<Review> findTop5ByOrderByRatingAsc();

    List<Review> findTop3ByRatingGreaterThanEqualOrderByRatingDescCreatedAtDesc(Integer rating);

    @Query("""
        SELECT AVG(CAST(r.rating AS double)) FROM Review r
        WHERE r.house.id = :houseId AND r.rating IS NOT NULL
    """)
    Double getAverageRatingForHouse(Long houseId);
}