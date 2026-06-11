package com.demo.controller;

import com.demo.dto.HouseStatsDto;
import com.demo.model.*;
import com.demo.model.enums.HouseType;
import com.demo.model.enums.Province;
import com.demo.model.enums.Role;
import com.demo.model.enums.StatusReserva;
import com.demo.service.HouseService;
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

    private final HouseService houseService;
    private final ReviewService reviewService;

    @GetMapping("/houses")
    public String houseList(Model model,
                            @RequestParam(required = false) StatusReserva reserve,
                            @RequestParam(required = false) Double pricePerNight,
                            @RequestParam(required = false) String title,
                            @RequestParam(required = false) Province province,
                            @RequestParam(required = false) HouseType houseType,
                            @RequestParam(required = false) Double minRating,
                            @RequestParam(required = false) Boolean active,
                            @RequestParam(required = false) Boolean favoritesOnly,
                            @AuthenticationPrincipal User user
    ) {
        model.addAttribute("provinces", houseService.getTopProvinces());

        @SuppressWarnings("unchecked")
        Set<Long> favoritesHouses = (Set<Long>) model.getAttribute("favoritesHouses");

        List<HouseStatsDto> housesStats = houseService.getHousesForCatalog(
                reserve, pricePerNight, title, province, houseType, minRating, active,
                favoritesOnly, user, favoritesHouses
        );

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
        Optional<House> houseOptional = houseService.findById(id);

        if (houseOptional.isPresent()) {
            // casa sí existe
            House house = houseOptional.get();
            house.setActive(false);
            houseService.save(house);

        }
        return "redirect:/houses";
    }

    // nuevo metodo para traer un solo restaurante por su id
    @GetMapping("houses/{id}")
    public String houseDetail(@PathVariable Long id, Model model, @AuthenticationPrincipal User user) {

        // buscar restaurante por su id: findById
        boolean isAdmin = user != null && user.getRole() == Role.ROLE_ADMIN;
        Optional<House> houseOptional = isAdmin
                ? houseService.findById(id)
                : houseService.findByIdAndActiveTrue(id);

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

        return "redirect:/houses";
    }


    @GetMapping("houses/new")
    public String newHouses(Model model) {
        model.addAttribute("house", new House());
        model.addAttribute("provinces", HouseService.PROVINCES);
        return "house/house-form";
    }

    @GetMapping("houses/edit/{id}")
    public String editHouse(@PathVariable Long id, Model model) {
        model.addAttribute("house", houseService.findById(id).orElseThrow());
        model.addAttribute("provinces", HouseService.PROVINCES);
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

        houseService.save(house);
        return "redirect:/houses/" + house.getId();
    }

}
