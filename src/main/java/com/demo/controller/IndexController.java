package com.demo.controller;

import com.demo.model.House;
import com.demo.repository.HouseRepository;
import com.demo.service.ReviewService;
import com.demo.repository.AmenityRepository;
import lombok.AllArgsConstructor;
import org.hibernate.mapping.Array;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class IndexController {
    private final ReviewService reviewService;
    private final HouseRepository houseRepository;
    private final AmenityRepository amenityRepository;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("reviews", reviewService.findTop5ByOrderByRatingAsc());
        
        java.util.List<House> topHouses = houseRepository.findTop3ByOrderByAverageRatingDesc();
        
        Map<Long, Double> houseRatings = new HashMap<>();
        for (House h : topHouses) {
            houseRatings.put(h.getId(), reviewService.getAverageRating(h.getId()));
        }
        
        model.addAttribute("houses", topHouses);
        model.addAttribute("houseRatings", houseRatings);
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
