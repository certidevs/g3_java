package com.demo.controller;

import com.demo.model.User;
import com.demo.model.House;
import com.demo.repository.BookingRepository;
import com.demo.repository.UserRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public class ControlPanelController {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public ControlPanelController(BookingRepository bookingRepository,
                             UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("panel-control/{id}")
    public String panelControl (Model model, @PathVariable Long id) {

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User validUser = user.get();

            // Casas que pone en alquiler
            List<House> listHouseHost = bookingRepository.houseBookingHost(id);
            // Casas alquiladas
            List<House> listHouseGuest = bookingRepository.housesBookingGuest(id);

            // Atributos pasados al HTML
            model.addAttribute("user",validUser);
            model.addAttribute("bookingHost",listHouseHost);
            model.addAttribute("bookingGuest",listHouseGuest);

            return "panel-control";
        }
        else {
            return "redirect:/index";
        }
    }
}
