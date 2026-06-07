package com.demo.repository;

import com.demo.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByUser_IdAndHouse_Id(Long userId, Long houseId);

    @Query("""
        SELECT f.house.id FROM Favorite f
        WHERE f.user.id = :userId
        AND f.house IS NOT NULL
    """)
    Set<Long> getHousesIdsByUserId(@Param("userId") Long userId);;
}