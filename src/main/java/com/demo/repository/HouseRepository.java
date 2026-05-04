package com.demo.repository;

import com.demo.model.House;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HouseRepository extends JpaRepository<House, Long> {

    //FILTRAR POR UBICACION DE CASA

    List<House> findByLocation(String location);
//
//    //FILTRAR POR ID
//
    List<House> findByIdOrderByIdAsc(Long id);
//
//    //FILTRAR POR PRECIO POR NOCHE
//
    List<House> findByPricePerNightBetween(Double pricePerNightStart, Double pricePerNightEnd);


    //FILTRAR REVIEW CON MEJORES RESEÑAS

}
