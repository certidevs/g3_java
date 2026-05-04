package com.demo.repository;

import com.demo.model.House;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HouseRepository extends JpaRepository<House, Long> {

    //FILTRAR POR UBICACION DE CASA

    List<House> findByLocation(String location);
//
//    //FILTRAR POR ID
    List<House> findAllByOrderByIdAsc();
//
//    List<House> findByIdOrderByIdAsc(Long id);
//
//    //FILTRAR POR PRECIO POR NOCHE
//
    List<House> findByPricePerNightBetween(Double pricePerNightStart, Double pricePerNightEnd);


    //para mostrar las casas activas
    List<House> findByActiveTrue();
    Optional<House> findByIdAndActiveTrue(Long id);


    //FILTRAR REVIEW CON MEJORES RESEÑAS

}
