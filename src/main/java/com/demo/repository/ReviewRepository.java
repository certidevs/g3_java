package com.demo.repository;

import com.demo.model.Review;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // TODO:
    // - Filtrar reviews por restaurantes ordenados por date descendente, tipo:
    //   List<Review> findByHouse_IdOrderByCreatedAtDesc(Long houseId);
    // - Filtrar reviews de un restaurante y por rating (o rango de rating), ordenadas por creationDate desc
    // - Considerar filtrar por rango between, por ejemplo de fechas o de rating, ordenadas por creationDate desc
}