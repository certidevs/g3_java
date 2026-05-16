package com.demo.controller;

import com.demo.model.Booking;
import com.demo.model.HouseRecommended;
import com.demo.model.User;
import com.demo.model.House;
import com.demo.repository.BookingFilterRepository;
import com.demo.repository.BookingRepository;
import com.demo.repository.HouseRecommendedRepository;
import com.demo.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class  ControlPanelController {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final BookingFilterRepository bookingFilterRepository;
    private final HouseRecommendedRepository houseRecommendedRepository;

    public ControlPanelController(BookingRepository bookingRepository,
                                  UserRepository userRepository,
                                  BookingFilterRepository bookingFilterRepository,
                                  HouseRecommendedRepository houseRecommendedRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.bookingFilterRepository = bookingFilterRepository;
        this.houseRecommendedRepository = houseRecommendedRepository;
    }

    @GetMapping("panel-control/{id}")
    public String panelControl(Model model, @PathVariable Long id) {

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User validUser = user.get();

            // Casas que pone en alquiler
            List<House> listHouseHost = bookingRepository.houseBookingHost(id);
            // Casas alquiladas
            List<House> listHouseGuest = bookingRepository.housesBookingGuest(id);

            // Reservas del Host
            List<Booking> listBookingHost = bookingRepository.bookingsHost(id);
            // Reservas del Guest
            List<Booking> listBookingGuest = bookingRepository.bookingsGuest(id);

            // Atributos de listas pasados al HTML
            model.addAttribute("user", validUser);

            model.addAttribute("listHouseHost", listHouseHost);
            model.addAttribute("listHouseGuest", listHouseGuest);

            model.addAttribute("listBookingsHost", listBookingHost);
            model.addAttribute("listBookingGuest", listBookingGuest);


            return "panel-control";
        } else {
            return "redirect:/index";
        }
    }

    // FILTRADO
    @GetMapping("panel-control-filter/{id}")
    public String listaFiltrada(Model model, @PathVariable Long id,
                                @RequestParam(required = false) LocalDateTime searchDate,
                                @RequestParam(required = false) Double price) {

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User validUser = user.get();

            List<House> housesHost = bookingFilterRepository.houseBookinHostFilter(id,price);
            List<House> housesGuest = bookingFilterRepository.houseBookingGuestFilter(id,price);
            List<Booking> bookingsHost = bookingFilterRepository.bookingsHostFilter(id,searchDate,price);
            List<Booking> bookingsGuest = bookingFilterRepository.bookingsGuestFilter(id,searchDate,price);

            // Atributos de listas pasados al HTML
            model.addAttribute("user", validUser);

            model.addAttribute("listHouseHost", housesHost);
            model.addAttribute("listHouseGuest", housesGuest);

            model.addAttribute("listBookingsHost", bookingsHost);
            model.addAttribute("listBookingGuest", bookingsGuest);

            return "panel-control";

        }
        else {
            return "redirect:/panel-control/" + id.toString();
        }

    }

    @PostMapping("recommended")
    public String addRecommendation (@ModelAttribute HouseRecommended houseRecommended) {

        houseRecommendedRepository.save(houseRecommended);

        return "redirect:/houses";

    }


}