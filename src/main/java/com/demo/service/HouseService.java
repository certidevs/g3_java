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

    public House saveOrUpdate(House house, User host) {
        boolean isNew = (house.getId() == null);
        if (isNew) {
            if (host != null) {
                house.setHost(host);
            }
            if (house.getHouseType() == null) {
                house.setHouseType(HouseType.CASA);
            }
            house.setActive(true);
            house.setReserve(StatusReserva.DISPONIBLE);
            house.setTimeRecommended(java.time.LocalDateTime.now());
            return houseRepository.save(house);
        } else {
            House existingHouse = houseRepository.findById(house.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Alojamiento no encontrado con id: " + house.getId()));

            existingHouse.setTitle(house.getTitle());
            existingHouse.setDescription(house.getDescription());
            existingHouse.setPricePerNight(house.getPricePerNight());
            existingHouse.setLocation(house.getLocation());
            existingHouse.setProvince(house.getProvince());
            existingHouse.setMaxGuests(house.getMaxGuests());
            existingHouse.setHouseType(house.getHouseType());
            existingHouse.setAmenities(house.getAmenities());
            if (house.getImageUrl() != null && !house.getImageUrl().isEmpty()) {
                existingHouse.setImageUrl(house.getImageUrl());
            }

            return houseRepository.save(existingHouse);
        }
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
            Set<Long> favoritesHouses,
            Integer maxGuests,
            Boolean rentedOnly,
            List<Long> rentedHouseIds,
            List<Long> amenityIds
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

        boolean filterRented = Boolean.TRUE.equals(rentedOnly);
        if (filterRented && (rentedHouseIds == null || rentedHouseIds.isEmpty())) {
            return new ArrayList<>();
        }

        List<Long> rentedIds = (rentedHouseIds != null && !rentedHouseIds.isEmpty())
                ? rentedHouseIds
                : List.of(-1L);

        List<Long> amenities = (amenityIds != null) ? amenityIds : List.of();
        Long amenitiesCount = (long) amenities.size();

        return houseRepository.findByReserveStats(
                reserve, pricePerNight, title, province, houseType, minRating, activeFilter,
                filterFavorites, favIds, maxGuests, filterRented, rentedIds, amenities, amenitiesCount
        );
    }
}
