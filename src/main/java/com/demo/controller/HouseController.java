package com.demo.controller;

import com.demo.model.House;
import com.demo.model.Review;
import com.demo.repository.HouseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller

public class HouseController {

    private final HouseRepository houseRepository;

    public HouseController(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    @GetMapping("/")
    public String home(Model model){
//        List<House>  houses = houseRepository.findAll();
//        model.addAttribute("houses", houses);
        return "home/index";

    }

    @GetMapping("/houses")
    public String houseList(Model model){
        List <House>  houses = houseRepository.findAll();
        model.addAttribute("houses", houses);
        return "house/house-list";

    }

    // nuevo metodo para traer un solo restaurante por su id
    @GetMapping("houses/{id}")
    public String houseDetail(@PathVariable Long id, Model model) {

        // buscar restaurante por su id: findById
        Optional<House> houseOptional = houseRepository.findById(id);
        if (houseOptional.isPresent()) {

            // casa sí existe
            House house = houseOptional.get();
            model.addAttribute("house", house);
            // opcional:
            // cargar los platos (Dish) de este restaurant en el model
//            List<Dish> platos = dishRepository.findByRestaurantIdOrderByPrice(restaurant.getId());
//            model.addAttribute("dishes", platos);

            // reviews
            //List<Review> reviews = reviewRepository.findAll();
//            List<Review> reviews = reviewRepository.findByRestaurant_IdOrderByCreationDateDesc(restaurant.getId());
//            model.addAttribute("reviews", reviews); // accesibles desde HTML

            return "house/house-detail";

        }
        // El restaurante NO existe
        // CUIDADO no apunta a HTML
        // APUNTA al Controller
        return "redirect:/house";
    }
}
