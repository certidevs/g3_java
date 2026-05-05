package com.demo.repository;

import com.demo.model.House;
import com.demo.model.StatusReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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



    //para mostrar las casas activas
    List<House> findByActiveTrue();
    Optional<House> findByIdAndActiveTrue(Long id);

    //FILTRO POR ESTADO DE RESERVA, PRECIO, PROVINCIA, TITULO
    @Query("""
    SELECT h FROM House h
    WHERE (:reserve IS NULL OR h.reserve = :reserve)
      AND (:price IS NULL OR h.pricePerNight <= :price)
      AND (:title IS NULL OR LOWER(h.title) LIKE LOWER(CONCAT('%', :title, '%')))
      AND (:province IS NULL OR LOWER(h.province) LIKE LOWER(CONCAT('%', :province, '%')))
    """)

    List<House> findByReserve(
            @Param("reserve") StatusReserva reserve,
            @Param("price") Double price,
            @Param("title") String title,
            @Param("province") String province

    );

    //FILTRAR REVIEW CON MEJORES RESEÑAS

}
