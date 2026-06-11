package com.demo.controller;

import com.demo.dto.HouseStatsDto;
import com.demo.model.*;
import com.demo.model.enums.HouseType;
import com.demo.model.enums.StatusReserva;
import com.demo.repository.HouseRepository;
import com.demo.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@AllArgsConstructor
public class HouseController {

    // TODO: Sería mejor en un HouseService
    private static final List<String> FORM_PROVINCES = Arrays.asList(
            "Madrid", "Barcelona", "Valencia", "Sevilla", "Málaga", "Bilbao", "Asturias", "Alicante", "Zaragoza"
    );

    private final HouseRepository houseRepository;
    private final ReviewService reviewService;

    @GetMapping("/houses")
    public String houseList(Model model,
                            @RequestParam(required = false) StatusReserva reserve,
                            @RequestParam(required = false) Double pricePerNight,
                            @RequestParam(required = false) String title,
                            @RequestParam(required = false) String province,
                            @RequestParam(required = false) HouseType houseType,
                            @RequestParam(required = false) Double minRating,
                            @RequestParam(required = false) Boolean active,
                            @RequestParam(required = false) Boolean favoritesOnly,
                            @AuthenticationPrincipal User user
    ) {
        // TODO: Casi todo esto se puede abstraer al HouseService si existiera, o probablemente usando un Dto para los RequestParam
        boolean isAdmin = user != null && user.getRole() == Role.ROLE_ADMIN;
        if (!isAdmin) {
            active = true;
        }

        model.addAttribute("provinces", houseRepository.getTopProvinces());

        boolean filterFavorites = Boolean.TRUE.equals(favoritesOnly);
        List<HouseStatsDto> housesStats;

        @SuppressWarnings("unchecked")
        Set<Long> favoritesHouses = (Set<Long>) model.getAttribute("favoritesHouses");

        if (filterFavorites && (favoritesHouses == null || favoritesHouses.isEmpty())) {
            housesStats = new ArrayList<>();
        } else {
            List<Long> favIds = (favoritesHouses != null && !favoritesHouses.isEmpty())
                    ? new ArrayList<>(favoritesHouses)
                    : List.of(-1L);

            housesStats = houseRepository.findByReserveStats(
                    reserve, pricePerNight, title, province, houseType, minRating, active,
                    filterFavorites, favIds
            );
        }

        model.addAttribute("houses", housesStats);
        model.addAttribute("selectedProvince", province);
        model.addAttribute("selectedHouseType", houseType);
        model.addAttribute("selectedPricePerNight", pricePerNight);
        model.addAttribute("selectedMinRating", minRating);
        model.addAttribute("selectedActive", active);
        model.addAttribute("selectedFavoritesOnly", favoritesOnly);

        /*
        Map<Long, Double> houseRatings = new HashMap<>();
        for (House h : houseStatus) {
            houseRatings.put(h.getId(), reviewService.getAverageRating(h.getId()));
        }
        model.addAttribute("houseRatings", houseRatings);
        // Use housesStats
        model.addAttribute("houseRatings",
                housesStats.stream().collect(Collectors.toMap(HouseStats::id, HouseStats::averageRating))
        );
         */

        return "house/house-list";
    }

    @GetMapping("/houses/deactivate/{id}")
    public String houseDeactivate(@PathVariable Long id, Model model) {
        Optional<House> houseOptional = houseRepository.findById(id);

        if (houseOptional.isPresent()) {
            // casa sí existe
            House house = houseOptional.get();
            house.setActive(false);
            houseRepository.save(house);

        }
        return "redirect:/houses";
    }

    // nuevo metodo para traer un solo restaurante por su id
    @GetMapping("houses/{id}")
    public String houseDetail(@PathVariable Long id, Model model) {

        // buscar restaurante por su id: findById
//        Optional<House> houseOptional = houseRepository.findById(id);
        Optional<House> houseOptional = houseRepository.findByIdAndActiveTrue(id);

        if (houseOptional.isPresent()) {

            // casa sí existe
            House house = houseOptional.get();
            model.addAttribute("house", house);

            Double averageRating = reviewService.getAverageRating(house.getId());
            model.addAttribute("averageRating", averageRating);

            // reviews
            List<Review> reviews = reviewService.findByHouseIdOrderByCreatedAtDesc(house.getId());
            model.addAttribute("reviews", reviews); // accesibles desde HTML

            return "house/house-detail";

        }

        return "redirect:/house";
    }


    @GetMapping("houses/new")
    public String newHouses(Model model) {
        model.addAttribute("house", new House());
        model.addAttribute("provinces", FORM_PROVINCES);
        return "house/house-form";
    }

    @GetMapping("houses/edit/{id}")
    public String editHouse(@PathVariable Long id, Model model) {
        model.addAttribute("house", houseRepository.findById(id).orElseThrow());
        model.addAttribute("provinces", FORM_PROVINCES);
        return "house/house-form";
    }


    @PostMapping("/houses")
    public String createHouse(@ModelAttribute House house,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              RedirectAttributes redirectAttributes) throws IOException {

        long maxSize = 5 * 1024 * 1024L; // 5 MB

        if (!imageFile.isEmpty()) {
            if (imageFile.getSize() > maxSize) {
                redirectAttributes.addFlashAttribute("error", "El archivo es demasiado grande. Máximo 5 MB.");
                return "redirect:/houses/new";
            }

            String fileName = imageFile.getOriginalFilename();
            if (fileName == null || !fileName.toLowerCase().endsWith(".png")) {
                redirectAttributes.addFlashAttribute("error", "Solo se permiten archivos PNG.");
                return "redirect:/houses/new";
            }

            Path path = Paths.get(System.getProperty("user.dir"), "uploads", fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, imageFile.getBytes());
            house.setImageUrl(fileName);
        }

        houseRepository.save(house);
        return "redirect:/houses/" + house.getId();
    }

}
