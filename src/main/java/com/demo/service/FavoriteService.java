package com.demo.service;

import com.demo.model.Favorite;
import com.demo.model.House;
import com.demo.model.User;
import com.demo.repository.FavoriteRepository;
import com.demo.service.HouseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final HouseService houseService;

    public Set<Long> getFavoriteHouseIds(User user) {
        return favoriteRepository.getHousesIdsByUserId(user.getId());
    }

    public boolean toggleFavoriteHouse(User user, Long houseId) {
        Optional<Favorite> favoriteOpt = favoriteRepository.findByUser_IdAndHouse_Id(user.getId(), houseId);

        if (favoriteOpt.isPresent()) {
            favoriteRepository.delete(favoriteOpt.get());
            return false;
        }

        House house = houseService.findById(houseId).orElseThrow();
        favoriteRepository.save(Favorite.builder()
                .user(user)
                .house(house)
                .build());
        return true;
    }
}
