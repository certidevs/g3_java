package com.demo.dto;


import com.demo.model.enums.HouseType;
import com.demo.model.enums.Province;

public record HouseStatsDto(
        Long id,
        String title,
        Double pricePerNight,
        Province province,
        Integer maxGuests,
        HouseType houseType,
        String imageUrl,
        Boolean active,
        Double averageRating,
        Long reviewCount
) {}
