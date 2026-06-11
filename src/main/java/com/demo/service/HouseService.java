package com.demo.service;

import com.demo.dto.HouseStatsDto;
import com.demo.model.House;
import com.demo.model.enums.HouseType;
import com.demo.model.enums.Role;
import com.demo.model.enums.StatusReserva;
import com.demo.model.User;
import com.demo.repository.HouseRepository;
import com.demo.model.enums.Province;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class HouseService {
    public static final List<Province> PROVINCES = Arrays.asList(Province.values());

    private final HouseRepository houseRepository;

    /* public List<House> findTop3ByOrderByAverageRatingDesc() {
        return houseRepository.findTop3ByOrderByAverageRatingDesc();
    } */

    public List<HouseStatsDto> findTop3HousesWithStats() {
        return houseRepository.findTop3HousesWithStats();
    }

    public List<Province> getTopProvinces() {
        return houseRepository.getTopProvinces();
    }

    public Optional<House> findById(Long id) {
        return houseRepository.findById(id);
    }

    public Optional<House> findByIdAndActiveTrue(Long id) {
        return houseRepository.findByIdAndActiveTrue(id);
    }

    public House save(House house) {
        return houseRepository.save(house);
    }

    public List<HouseStatsDto> getHousesForCatalog(
            StatusReserva reserve,
            Double pricePerNight,
            String title,
            Province province,
            HouseType houseType,
            Double minRating,
            Boolean active,
            Boolean favoritesOnly,
            User user,
            Set<Long> favoritesHouses
    ) {
        boolean isAdmin = user != null && user.getRole() == Role.ROLE_ADMIN;
        Boolean activeFilter = true;
        if (isAdmin) {
            activeFilter = active;
        }

        boolean filterFavorites = Boolean.TRUE.equals(favoritesOnly);
        if (filterFavorites && (favoritesHouses == null || favoritesHouses.isEmpty())) {
            return new ArrayList<>();
        }

        List<Long> favIds = (favoritesHouses != null && !favoritesHouses.isEmpty())
                ? new ArrayList<>(favoritesHouses)
                : List.of(-1L);

        return houseRepository.findByReserveStats(
                reserve, pricePerNight, title, province, houseType, minRating, activeFilter,
                filterFavorites, favIds
        );
    }
}
