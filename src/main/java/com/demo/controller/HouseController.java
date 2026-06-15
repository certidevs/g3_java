package com.demo.controller;

import com.demo.dto.HouseStatsDto;
import com.demo.model.*;
import com.demo.model.enums.HouseType;
import com.demo.model.enums.Province;
import com.demo.model.enums.Role;
import com.demo.model.enums.StatusReserva;
import com.demo.service.FileService;
import com.demo.service.HouseService;
import com.demo.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@AllArgsConstructor
public class HouseController {

    private final HouseService houseService;
    private final ReviewService reviewService;
    private final FileService fileService;
    private final com.demo.service.BookingService bookingService;
    private final com.demo.service.RecommendedService recommendedService;
    private final com.demo.repository.AmenityRepository amenityRepository;



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
                            @RequestParam(required = false) Integer maxGuests,
                            @RequestParam(required = false) Boolean rentedOnly,
                            @RequestParam(required = false) List<Long> amenityIds,
                            @AuthenticationPrincipal User user
    ) {
        model.addAttribute("provinces", houseService.getTopProvinces());
        model.addAttribute("allAmenities", amenityRepository.findAll());

        List<Long> rentedHouseIds = new ArrayList<>();
        if (user != null) {
            List<House> rentedProperties = bookingService.getGuestProperties(user.getId());
            rentedHouseIds = rentedProperties.stream().map(House::getId).toList();
        }

        @SuppressWarnings("unchecked")
        Set<Long> favoritesHouses = (Set<Long>) model.getAttribute("favoritesHouses");

        List<HouseStatsDto> housesStats = houseService.getHousesForCatalog(
                reserve, pricePerNight, title, province, houseType, minRating, active,
                favoritesOnly, user, favoritesHouses, maxGuests, rentedOnly, rentedHouseIds, amenityIds
        );

        model.addAttribute("houses", housesStats);
        model.addAttribute("selectedProvince", province);
        model.addAttribute("selectedHouseType", houseType);
        model.addAttribute("selectedPricePerNight", pricePerNight);
        model.addAttribute("selectedMinRating", minRating);
        model.addAttribute("selectedActive", active);
        model.addAttribute("selectedFavoritesOnly", favoritesOnly);
        model.addAttribute("selectedMaxGuests", maxGuests);
        model.addAttribute("selectedRentedOnly", rentedOnly);
        model.addAttribute("selectedAmenityIds", amenityIds != null ? amenityIds : new ArrayList<Long>());

        return "house/house-list";
    }

    @GetMapping("/houses/deactivate/{id}")
    public String houseDeactivate(@PathVariable Long id) {
        Optional<House> houseOptional = houseService.findById(id);

        if (houseOptional.isPresent()) {
            // casa sí existe
            House house = houseOptional.get();
            house.setActive(false);
            houseService.save(house);

        }
        return "redirect:/houses#house-card-" + id;
    }

    @GetMapping("/houses/activate/{id}")
    public String houseActivate(@PathVariable Long id) {
        Optional<House> houseOptional = houseService.findById(id);

        if (houseOptional.isPresent()) {
            // casa sí existe
            House house = houseOptional.get();
            house.setActive(true);
            houseService.save(house);

        }
        return "redirect:/houses#house-card-" + id;
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

            boolean hasVisited = user != null && bookingService.hasUserVisitedHouse(user.getId(), house.getId());
            model.addAttribute("hasVisited", hasVisited);

            boolean hasRecommended = user != null && recommendedService.hasUserRecommendedHouse(user.getId(), house.getId());
            model.addAttribute("hasRecommended", hasRecommended);

            return "house/house-detail";


        }

        return "redirect:/houses";
    }



    @GetMapping("houses/new")
    public String newHouses(Model model) {
        if (!model.containsAttribute("house")) {
            model.addAttribute("house", new House());
        }
        model.addAttribute("provinces", HouseService.PROVINCES);
        model.addAttribute("houseTypes", HouseType.values());
        model.addAttribute("allAmenities", amenityRepository.findAll());
        return "house/house-form";
    }

    @GetMapping("houses/edit/{id}")
    public String editHouse(@PathVariable Long id, Model model) {
        if (!model.containsAttribute("house")) {
            model.addAttribute("house", houseService.findById(id).orElseThrow());
        }
        model.addAttribute("provinces", HouseService.PROVINCES);
        model.addAttribute("houseTypes", HouseType.values());
        model.addAttribute("allAmenities", amenityRepository.findAll());
        return "house/house-form";
    }

    @PostMapping("/houses")
    public String createHouse(@ModelAttribute House house,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              @AuthenticationPrincipal User user,
                              RedirectAttributes redirectAttributes) {
        boolean isNew = (house.getId() == null);
        String currentImageUrl = house.getImageUrl();

        if (isNew && imageFile.isEmpty() && (currentImageUrl == null || currentImageUrl.isEmpty())) {
            redirectAttributes.addFlashAttribute("error", "Debe seleccionar una imagen de portada para el alojamiento.");
            redirectAttributes.addFlashAttribute("house", house);
            return isNew ? "redirect:/houses/new" : "redirect:/houses/edit/" + house.getId();
        }

        try {
            if (!imageFile.isEmpty()) {
                String filename = fileService.store(imageFile);
                if (filename != null) {
                    house.setImageUrl(filename);
                }
            }

            House savedHouse = houseService.saveOrUpdate(house, user);
            return "redirect:/houses/" + savedHouse.getId();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("house", house);
            return isNew ? "redirect:/houses/new" : "redirect:/houses/edit/" + house.getId();
        }
    }

}
