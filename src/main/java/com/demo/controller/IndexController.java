package com.demo.controller;

import com.demo.model.Amenity;
import com.demo.model.House;
import com.demo.model.Review;
import com.demo.repository.HouseRepository;
import com.demo.service.ReviewService;
import com.demo.repository.AmenityRepository;
import lombok.AllArgsConstructor;
import org.hibernate.mapping.Array;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
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
        List<House> topHouses = houseRepository.findTop3ByOrderByAverageRatingDesc();
        model.addAttribute("houses", topHouses);

        Map<Long, Double> houseRatings = new HashMap<>();
        for (House h : topHouses) {
            houseRatings.put(h.getId(), reviewService.getAverageRating(h.getId()));
        }
        model.addAttribute("houseRatings", houseRatings);

        List<Review> indexReviews = new ArrayList<>(reviewService.findTop3Reviews(4));
        model.addAttribute("reviews", indexReviews);

        List<Amenity> amenities = amenityRepository.findAll();
        model.addAttribute("amenities", amenities);

        // Placeholders para casas
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

        // Placeholders para reseñas
        model.addAttribute("reviews_titles", new String[]{
                "Lugar Soñado",
                "Muy recomendable",
                "Pura tranquilidad"
        });
        model.addAttribute("reviews_comments", new String[]{
                "¡Increíble! Perfecta para la familia.",
                "Piscina y vistas espectaculares.",
                "Ideal para desconectar."
        });
        model.addAttribute("reviews_users", new String[]{
                "María González",
                "Juan Pérez",
                "Ana López"
        });

        return "index";
    }
}
