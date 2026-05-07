package com.demo.controller;

import com.demo.model.Booking;
import com.demo.model.House;
import com.demo.model.StatusBooking;
import com.demo.model.User;
import com.demo.repository.BookingRepository;
import com.demo.repository.UserRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ser.std.DelegatingSerializer;

import java.time.LocalDateTime;
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


    // BOOKING ID.
    @GetMapping("/booking/{id}")
    public String getBookingById (Model model,@PathVariable Long id)
    {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isPresent()) {

            Booking validBooking = booking.get();

            model.addAttribute("booking", validBooking);
            return "/host/booking-detail";

        }
        return "redirect:/index";

    }




    // LADO ANFITRION
    @GetMapping("/host/pending/{id}")
    public String listHostPending (Model model, @PathVariable Long id)
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

    @GetMapping("/host/confirmed/{id}")
    public String listHostConfirmed (Model model, @PathVariable Long id)
    {

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User validUser = user.get();


            // Reservas del Host
            List<Booking> listBookingHost = bookingRepository.bookingsHostConfirmed(id);


            // Atributos de listas pasados al HTML
            model.addAttribute("user",validUser);
            model.addAttribute("listBookingsHostConfirmed",listBookingHost);

            return "/host/booking-list-confirmed-host";
        }
        else {
            return "redirect:/index";
        }

    }

    @GetMapping("/host/cancelled/{id}")
    public String listHostCancelled (Model model, @PathVariable Long id)
    {

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User validUser = user.get();


            // Reservas del Host
            List<Booking> listBookingHost = bookingRepository.bookingsHostCancelled(id);


            // Atributos de listas pasados al HTML
            model.addAttribute("user",validUser);
            model.addAttribute("listBookingsHostCancelled",listBookingHost);

            return "/host/booking-list-cancelled-host";
        }
        else {
            return "redirect:/index";
        }

    }

    @GetMapping("/host/completed/{id}")
    public String listHostCompleted (Model model, @PathVariable Long id)
    {

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User validUser = user.get();


            // Reservas del Host
            List<Booking> listBookingHost = bookingRepository.bookingsHostCompleted(id);


            // Atributos de listas pasados al HTML
            model.addAttribute("user",validUser);
            model.addAttribute("listBookingsHostCompleted",listBookingHost);

            return "/host/booking-list-completed-host";
        }
        else {
            return "redirect:/index";
        }

    }


    @GetMapping("/booking/from-pending-to-confirmed/{id}")
    // id del booking
    public String actionFromPendingToConfirmed (@PathVariable Long id, Model model) {

        Optional<Booking> bookingOptional = bookingRepository.findById(id);
            if (bookingOptional.isPresent()) {
                Booking bookingPresent = bookingOptional.get();
                bookingPresent.setStatusbooking(StatusBooking.CONFIRMED);
                bookingRepository.save(bookingPresent);
                User user = bookingPresent.getUserHouse().getHost();
                return "redirect:/host/pending/" + user.getId();
            }

            return "redirect:/houses";

    }

    @GetMapping("/booking/from-pending-to-cancelled/{id}")
    // id del booking
    public String actionFromPendingToCancelled (@PathVariable Long id, Model model) {

        Optional<Booking> bookingOptional =
                bookingRepository.findById(id);
        if (bookingOptional.isPresent()) {
            Booking bookingPresent = bookingOptional.get();
            bookingPresent.setStatusbooking(StatusBooking.CANCELLED);
            bookingRepository.save(bookingPresent);
            User user = bookingPresent.getUserHouse().getHost();
            return "redirect:/host/cancelled/" + user.getId();
        }

        return "redirect:/houses";


    }

    @GetMapping("/booking/from-confirmed-to-cancelled/{id}")
    // id del booking
    public String actionFromConfirmedToCancelled (@PathVariable Long id, Model model) {

        Optional<Booking> bookingOptional =
                bookingRepository.findById(id);
        if (bookingOptional.isPresent()) {
            Booking bookingPresent = bookingOptional.get();
            bookingPresent.setStatusbooking(StatusBooking.CANCELLED);
            bookingRepository.save(bookingPresent);
            User user = bookingPresent.getUserHouse().getHost();
            return "redirect:/host/cancelled/" + user.getId();

        }
        return "redirect:/houses";

    }

    @GetMapping("/booking/from-confirmed-to-completed/{id}")
    public String actionFromConfirmedToCompleted (@PathVariable Long id,Model model) {

        Optional<Booking> bookingOptional =
                bookingRepository.findById(id);
        if (bookingOptional.isPresent()) {
            Booking bookingPresent = bookingOptional.get();
            bookingPresent.setStatusbooking(StatusBooking.COMPLETED);
            bookingRepository.save(bookingPresent);
            User user=bookingPresent.getUserHouse().getHost();

            return "redirect:/host/completed/" + user.getId();

        }

        return "redirect:/houses";

    }

    @PostMapping("booking/update-dates")
    public String updateBooking(
            @RequestParam Long id,
            @RequestParam LocalDateTime checkin,
            @RequestParam LocalDateTime checkout
    ) {
        Booking booking = bookingRepository.findById(id).orElseThrow();
        booking.setCheckin(checkin);
        booking.setCheckout(checkout);
        bookingRepository.save(booking);
        //        Enviando al detalle
        return "redirect:/booking/" + booking.getId();
        //      Enviar al listado seria ...
        // return "redirect:/restaurantes";
    }

}
