package com.demo.controller;

import com.demo.model.Booking;
import com.demo.model.House;
import com.demo.model.StatusBooking;
import com.demo.repository.BookingRepository;
import com.demo.repository.HouseRepository;
import com.demo.repository.UserRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller

public class BookingController {

    private final BookingRepository bookingRepository;

    public BookingController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    // LADO HUESPED

    @GetMapping("/bookings/viewbytype/pending/{id}")
    public String bookingPendings (Model model, @PathVariable Long id){

        // Buscamos el Usuario por id.
        //Optional<User> user
        List<Booking> bookings = bookingRepository.findPendings(id);
        List<Booking> bookingsConfirmed = bookingRepository.findConfirmed(id);

        model.addAttribute("bookingsPendings",bookings);
        model.addAttribute("bookingsConfirmed",bookingsConfirmed);

        return "booking-list-pending";

    }



    // LADO ANFITRION . HOST
    @GetMapping("/bookings/host/pending")
    public String bookingPendingsHost (Model model){

        List<Booking> bookings = bookingRepository.findPendingHost();

        model.addAttribute("bookingsPendings",bookings);

        return "booking-list-pending-host";

    }

    @GetMapping("/bookings/host/confirmed")
    public String bookingConfirmedHost (Model model){

        List<Booking> bookings = bookingRepository.findConfirmedHost();

        model.addAttribute("bookingsConfirmed",bookings);

        return "booking-list-confirmed-host";

    }

}
