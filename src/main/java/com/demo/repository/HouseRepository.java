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
    List<House> findByLocation(String location);
//
//    //FILTRAR POR ID
      List<House> findAllByOrderByIdAsc();
//    List<House> findByIdOrderByIdAsc(Long id);
//
//    //FILTRAR POR PRECIO POR NOCHE
     List<House> findByPricePerNightBetween(Double pricePerNightStart, Double pricePerNightEnd);


    //para mostrar las casas activas
    List<House> findByActiveTrue();
    Optional<House> findByIdAndActiveTrue(Long id);

    //FILTRO POR ESTADO DE RESERVA

    @Query("SELECT h FROM House h WHERE (:StatusReserva IS NULL OR h.reserve = :StatusReserva)")
    List<House> findByReserve(@Param("StatusReserva") StatusReserva StatusReserva);
//    List<House> findByReserve(StatusReserva reserve);

//   @Query("SELECT h FROM House h WHERE h.reserve = true AND (:StatusReserva IS NULL OR h.reserve =:StatusReserve)")
//
//    List<House> findByReserve(@Param("StatusReserva") StatusReserva StatusReserva);



    //FILTRAR REVIEW CON MEJORES RESEÑAS

}
