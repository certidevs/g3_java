package com.demo.controller;

import com.demo.model.User;
import com.demo.service.FavoriteService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/favorites")
@AllArgsConstructor
class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping("/toggle")
    public String toggleFavorite(
            @RequestParam Long id,
            @RequestParam(defaultValue = "/houses") String redirectUrl,
            @AuthenticationPrincipal User currentUser,
            RedirectAttributes redirectAttributes) {
        if (currentUser != null) {
            boolean isFavorite = favoriteService.toggleFavoriteHouse(currentUser, id);
            redirectAttributes.addFlashAttribute("message", isFavorite ? "Casa añadida a favoritos" : "Casa eliminada de favoritos");
        }
        return "redirect:" + redirectUrl;
    }
}
