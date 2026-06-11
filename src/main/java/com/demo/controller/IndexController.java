package com.demo.controller;

import com.demo.dto.HouseStatsDto;
import com.demo.model.Amenity;
import com.demo.model.Review;
import com.demo.repository.AmenityRepository;
import com.demo.service.HouseService;
import com.demo.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class IndexController {
    private final ReviewService reviewService;
    private final HouseService houseService;
    private final AmenityRepository amenityRepository;

    @GetMapping("/")
    public String index(Model model) {
        List<HouseStatsDto> topHouses = houseService.findTop3HousesWithStats();
        model.addAttribute("houses", topHouses);
        model.addAttribute("housesProvinces", houseService.getTopProvinces());

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
