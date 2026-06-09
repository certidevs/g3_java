package com.demo.controller;

import com.demo.model.User;
import com.demo.repository.HouseRepository;
import com.demo.service.FavoriteService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Set;

@ControllerAdvice
@AllArgsConstructor
class GlobalModelAttributes {
    private final FavoriteService favoriteService;
    private final HouseRepository houseRepository;

    @ModelAttribute("favoritesHouses")
    public Set<Long> getFavoriteHouseIds(@AuthenticationPrincipal User currentUser) {
        if (currentUser != null) {
            return favoriteService.getFavoriteHouseIds(currentUser);
        }
        return Set.of();
    }

    // para formularios de crear y editar casa
    @ModelAttribute("provinces")
    public List<String> getProvinces() {
        return List.of("Madrid", "Barcelona", "Asturias");
    }
    // para filtros
    @ModelAttribute("houseProvinces")
    public List<String> getProvincesWithHouses() {
        return houseRepository.getTopProvinces();
    }

}
