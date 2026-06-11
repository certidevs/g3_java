package com.demo.dto;


import com.demo.model.enums.HouseType;

public record HouseStatsDto(
        Long id,
        String title,
        Double pricePerNight,
        String province,
        Integer maxGuests,
        HouseType houseType,
        String imageUrl,
        Boolean active,
        Double averageRating,
        Long reviewCount
) {}
