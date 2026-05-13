package com.demo.controller;

import com.demo.model.Booking;
import com.demo.model.House;
import com.demo.model.User;
import com.demo.repository.HouseRepository;
import com.demo.repository.ReviewRepository;
import com.demo.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class OrderController {
    private final HouseRepository houseRepository;
    private final UserRepository userRepository;
//    private final ReviewRepository reviewRepository;

    public OrderController(HouseRepository houseRepository, UserRepository userRepository,ReviewRepository reviewRepository) {
        this.houseRepository = houseRepository;
        this.userRepository = userRepository;
//        this.reviewRepository = reviewRepository;
    }

    @GetMapping("/orders/new")
    public String newOrder(Model model, @RequestParam Long houseId) {
        House house = houseRepository.findById(houseId).orElseThrow();
        Booking order = new Booking();
        order.setUserHouse(house);
        model.addAttribute("booking", order);
        // Agregar esto:
        List<User> usuarios = userRepository.findAll();  // ← Si tienes UserRepository
        model.addAttribute("usuarios", usuarios);
        return "host/booking-form";

    }
}
