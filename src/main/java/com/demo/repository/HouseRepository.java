package com.demo.repository;

import com.demo.dto.HouseStatsDto;
import com.demo.model.House;
import com.demo.model.HouseType;
import com.demo.model.StatusReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface HouseRepository extends JpaRepository<House, Long> {

    //FILTRAR POR UBICACION DE CASA
//    List<House> findByLocation(String location);
//    FILTRAR POR ID
//    List<House> findAllByOrderByIdAsc();
//    List<House> findByIdOrderByIdAsc(Long id);
//    //FILTRAR POR PRECIO POR NOCHE
//     List<House> findByPricePerNightBetween(Double pricePerNightStart, Double pricePerNightEnd);

    List<House> findTop3ByActiveTrue();

    //para mostrar las casas activas
    List<House> findByActiveTrue();

    Optional<House> findByIdAndActiveTrue(Long id);

    @Query("""
            SELECT h FROM House h
            LEFT JOIN Review r ON r.house = h
            WHERE (:active IS NULL OR h.active = :active)
              AND (:reserve IS NULL OR h.reserve = :reserve)
              AND (:price IS NULL OR h.pricePerNight <= :price)
              AND (:title IS NULL OR :title = '' OR LOWER(h.title) LIKE LOWER(CONCAT('%', :title, '%')))
              AND (:province IS NULL OR :province = '' OR LOWER(h.province) LIKE LOWER(CONCAT('%', :province, '%')))
              AND (:houseType IS NULL OR h.houseType = :houseType)
              AND (:filterFavorites = false OR h.id IN :favIds)
            GROUP BY h
            HAVING (:minRating IS NULL
                    OR (:minRating = 0.0 AND COUNT(r) > 0)
                    OR (COALESCE(AVG(CAST(r.rating AS double)), 0.0) >= :minRating AND COUNT(r) > 0))
            """)
    List<House> findByReserve(
            @Param("reserve") StatusReserva reserve,
            @Param("price") Double price,
            @Param("title") String title,
            @Param("province") String province,
            @Param("houseType") HouseType houseType,
            @Param("minRating") Double minRating,
            @Param("active") Boolean active,
            @Param("filterFavorites") boolean filterFavorites,
            @Param("favIds") Collection<Long> favIds
    );

    @Query("""
            SELECT new com.demo.dto.HouseStatsDto(
                        h.id as id,
                        h.title as title,
                        h.pricePerNight as pricePerNight,
                        h.province as province,
                        h.maxGuests as maxGuests,
                        h.houseType as houseType,
                        h.imageUrl as imageUrl,
                        h.active as active,
                        COALESCE(AVG(CAST(r.rating AS double)), 0.0) as averageRating,
                        COUNT(r) as reviewCount
            )
            FROM House h
            LEFT JOIN Review r ON r.house = h
            WHERE (:active IS NULL OR h.active = :active)
              AND (:reserve IS NULL OR h.reserve = :reserve)
              AND (:price IS NULL OR h.pricePerNight <= :price)
              AND (:title IS NULL OR :title = '' OR LOWER(h.title) LIKE LOWER(CONCAT('%', :title, '%')))
              AND (:province IS NULL OR :province = '' OR LOWER(h.province) LIKE LOWER(CONCAT('%', :province, '%')))
              AND (:houseType IS NULL OR h.houseType = :houseType)
              AND (:filterFavorites = false OR h.id IN :favIds)
            GROUP BY h
            HAVING (:minRating IS NULL
                    OR (:minRating = 0.0 AND COUNT(r) > 0)
                    OR (COALESCE(AVG(CAST(r.rating AS double)), 0.0) >= :minRating AND COUNT(r) > 0))
            """)
    List<HouseStatsDto> findByReserveStats(
            @Param("reserve") StatusReserva reserve,
            @Param("price") Double price,
            @Param("title") String title,
            @Param("province") String province,
            @Param("houseType") HouseType houseType,
            @Param("minRating") Double minRating,
            @Param("active") Boolean active,
            @Param("filterFavorites") boolean filterFavorites,
            @Param("favIds") Collection<Long> favIds
    );

    //FILTRAR REVIEW CON MEJORES RESEÑAS
    @Query("""
            SELECT h FROM House h
            LEFT JOIN Review r ON r.house = h
            WHERE (h.active IS NOT NULL OR h.active = true)
            GROUP BY h
            ORDER BY COALESCE(AVG(CAST(r.rating AS double)), 0.0) DESC
            """)
    List<House> findTop3ByOrderByAverageRatingDesc();


    @Query("""
            select distinct h.province from House h where h.province is not null
            """)
    List<String> getTopProvinces();
}
