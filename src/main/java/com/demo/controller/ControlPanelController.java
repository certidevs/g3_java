package com.demo.controller;

import com.demo.model.*;
import com.demo.service.BookingService;
import com.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class ControlPanelController {

    private final BookingService bookingService;
    private final UserService userService;
    // private final HouseRecommendedRepository houseRecommendedRepository;

    @GetMapping("panel-control/{userId}")
    public String panelControl(Model model, @PathVariable Long userId,
                               @RequestParam(required = false) String hostStatus,
                               @RequestParam(required = false) String guestStatus,
                               @AuthenticationPrincipal User currentUser) {

        if (currentUser.getRole() != Role.ROLE_ADMIN && !userId.equals(currentUser.getId())) {
            return "redirect:/panel-control/" + currentUser.getId();
        }

        Optional<User> user = userService.findById(userId);
        if (user.isPresent()) {
            User validUser = user.get();

            List<House> listHouseHost = bookingService.getHostProperties(userId);
            List<House> listHouseGuest = bookingService.getGuestProperties(userId);

            List<Booking> listBookingHost = bookingService.getHostBookings(userId, hostStatus);
            List<Booking> listBookingGuest = bookingService.getGuestBookings(userId, guestStatus);

            model.addAttribute("user", validUser);
            model.addAttribute("listHouseHost", listHouseHost);
            model.addAttribute("listHouseGuest", listHouseGuest);
            model.addAttribute("listBookingsHost", listBookingHost);
            model.addAttribute("listBookingGuest", listBookingGuest);
            model.addAttribute("selectedHostStatus", hostStatus);
            model.addAttribute("selectedGuestStatus", guestStatus);

            return "user/panel-control";
        } else {
            return "redirect:/index";
        }
    }

    // FILTRADO
    @GetMapping("panel-control-filter/{id}")
    public String listaFiltrada(Model model, @PathVariable Long id,
                                @RequestParam(required = false) LocalDateTime searchDate,
                                @RequestParam(required = false) Double price) {

        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            User validUser = user.get();

            List<House> housesHost = bookingService.getHostProperties(id, price);
            List<House> housesGuest = bookingService.getGuestProperties(id, price);
            List<Booking> bookingsHost = bookingService.getHostBookings(id, searchDate, price);
            List<Booking> bookingsGuest = bookingService.getGuestBookings(id, searchDate, price);

            // Atributos de listas pasados al HTML
            model.addAttribute("user", validUser);

            model.addAttribute("listHouseHost", housesHost);
            model.addAttribute("listHouseGuest", housesGuest);

            model.addAttribute("listBookingsHost", bookingsHost);
            model.addAttribute("listBookingGuest", bookingsGuest);

            return "user/panel-control";

        } else {
            return "redirect:/panel-control/" + id;
        }

    }


}