package com.demo.controller;

import com.demo.model.Booking;
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

    // LADO HUESPED

    @GetMapping("/bookings/viewbytype/pending/{id}")
    public String bookingPendings (Model model, @PathVariable Long id){

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {

            User validUser = user.get();

            // Buscamos el Usuario por id.
            //Optional<User> user
            List<Booking> bookings = bookingRepository.findPendings(id);
            List<Booking> bookingsConfirmed = bookingRepository.findConfirmed(id);

            model.addAttribute("bookingsPendings",bookings);
            model.addAttribute("bookingsConfirmed",bookingsConfirmed);
            model.addAttribute("user",validUser);

            return "guest/booking-list-pending";

        }
        else {
            return"redirect:/index";
        }

    }

    @GetMapping("/bookings/viewbytype/cancelled/{id}")
    public String bookingsCancelled (Model model, @PathVariable Long id) {
        List<Booking> bookings = bookingRepository.findCancelled(id);

        model.addAttribute("bookingsCancelled",bookings);

        return "guest/booking-list-cancelled";

    }

    // LADO ANFITRION . HOST
    @GetMapping("/bookings/host/pending/{id}")
    public String bookingPendingsHost (Model model, @PathVariable Long id){

        List<Booking> bookings = bookingRepository.findPendingHost(id);

        model.addAttribute("bookingsPendings",bookings);

        return "host/booking-list-pending-host";

    }

    @GetMapping("/bookings/host/confirmed/{id}")
    public String bookingConfirmedHost (Model model, @PathVariable Long id){

        List<Booking> bookings = bookingRepository.findConfirmedHost(id);

        model.addAttribute("bookingsConfirmed",bookings);

        return "host/booking-list-confirmed-host";

    }

}
