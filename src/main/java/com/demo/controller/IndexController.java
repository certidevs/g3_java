package com.demo.controller;

import com.demo.repository.HouseRepository;
import com.demo.repository.ReviewRepository;
import com.demo.repository.AmenityRepository;
import lombok.AllArgsConstructor;
import org.hibernate.mapping.Array;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class IndexController {
    private final ReviewRepository reviewRepository;
    private final HouseRepository houseRepository;
    private final AmenityRepository amenityRepository;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("reviews", reviewRepository.findTop5ByOrderByRatingAsc());
        model.addAttribute("houses", houseRepository.findTop3ByActiveTrue());
        model.addAttribute("amenities", amenityRepository.findAll());
        // Array con los textos definidos
        model.addAttribute("houses_titles", new String[]{
                "Escapadas inolvidables",
                "Relájate con estilo",
                "Naturaleza y tranquilidad"
        });
        model.addAttribute("houses_descriptions", new String[]{
                "Descubre casas únicas junto al mar.",
                "Casas con piscina privada para tu comodidad.",
                "Alquila casas rurales en entornos únicos."
        });
        return "index";
    }
}
