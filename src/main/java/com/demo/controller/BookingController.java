package com.demo.controller;

import com.demo.model.Booking;
import com.demo.model.House;
import com.demo.model.User;
import com.demo.repository.BookingRepository;
import com.demo.repository.UserRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class BookingController {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public BookingController(BookingRepository bookingRepository,
                             UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    // LADO ANFITRION
    @GetMapping("/host/pending/{id}")
    public String listaHostPendientes (Model model, @PathVariable Long id)
    {

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User validUser = user.get();


            // Reservas del Host
            List<Booking> listBookingHost = bookingRepository.bookingsHostPending(id);


            // Atributos de listas pasados al HTML
            model.addAttribute("user",validUser);
            model.addAttribute("listBookingsHostPending",listBookingHost);

            return "/host/booking-list-pending-host";
        }
        else {
            return "redirect:/index";
        }

    }

}
