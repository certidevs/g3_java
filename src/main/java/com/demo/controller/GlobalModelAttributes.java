package com.demo.controller;

import com.demo.model.User;
import com.demo.service.FavoriteService;
import com.demo.service.RecommendedService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Set;

@ControllerAdvice
@AllArgsConstructor
class GlobalModelAttributes {
    private final FavoriteService favoriteService;
    private final RecommendedService recommendedService;

    @ModelAttribute("favoritesHouses")
    public Set<Long> getFavoriteHouseIds(@AuthenticationPrincipal User currentUser) {
        if (currentUser != null) {
            return favoriteService.getFavoriteHouseIds(currentUser);
        }
        return Set.of();
    }

    @ModelAttribute("hasUnreadRecommendations")
    public boolean hasUnreadRecommendations(@AuthenticationPrincipal User currentUser) {
        if (currentUser != null) {
            return recommendedService.hasUnreadRecommendations(currentUser.getEmail(), currentUser.getTokenforRecommended());
        }
        return false;
    }
}
