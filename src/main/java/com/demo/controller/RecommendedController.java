package com.demo.controller;

import com.demo.model.House;
import com.demo.model.HouseRecommended;
import com.demo.model.User;
import com.demo.repository.BookingRepository;
import com.demo.repository.HouseRepository;
import com.demo.repository.UserRepository;
import com.demo.service.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class RecommendedController {

    private final HouseRepository houseRepository;
    private final UserRepository userRepository;

    public RecommendedController(
                             HouseRepository houseRepository,
                             UserRepository userRepository) {
        this.houseRepository = houseRepository;
        this.userRepository = userRepository;
    }


    @GetMapping("/recommended/{token}/{idHouse}/{idUsuario}")
    public String createRecommended (Model model,
                         @PathVariable String token,
                         @PathVariable Long idHouse,
                         @PathVariable Long idUsuario) {

        Optional<House> house = houseRepository.findById(idHouse);
        Optional<User> user = userRepository.findById(idUsuario);

        if (house.isPresent() && user.isPresent()) {

            House houseValid = house.get();
            User userValid = user.get();

            HouseRecommended recommendation = new HouseRecommended();
            recommendation.setIdHouseRecommended(houseValid.getId());
            recommendation.setTokenFrom(token);
            recommendation.setIdUsuario(userValid.getId());
            recommendation.setEmailFrom(userValid.getEmail());

            model.addAttribute("recommendation",recommendation);

            return "/guest/house-recommended";
        }
        else
        {
            return "redirect:/booking/" + idUsuario.toString();
        }

    }

}
