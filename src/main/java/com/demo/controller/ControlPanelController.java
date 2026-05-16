package com.demo.controller;

import com.demo.model.Booking;
import com.demo.model.User;
import com.demo.model.House;
import com.demo.repository.BookingFilterRepository;
import com.demo.repository.BookingRepository;
import com.demo.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class  ControlPanelController {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final BookingFilterRepository bookingFilterRepository;

    public ControlPanelController(BookingRepository bookingRepository,
                                  UserRepository userRepository,
                                  BookingFilterRepository bookingFilterRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.bookingFilterRepository = bookingFilterRepository;
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
                                @RequestParam(required = false) LocalDate minDate,
                                @RequestParam(required = false) LocalDate maxDate,
                                @RequestParam(required = false) Double precio) {

        System.out.println(id.toString());
        //System.out.println(minDate.toString());
        //System.out.println(maxDate.toString());
        System.out.println(precio.toString());

        List<House> houseshost = bookingFilterRepository.bookingsFilterHouseHost(id,minDate,maxDate,precio);

        List<House> houserGuest = bookingFilterRepository.

        //List<Booking> bookings = bookingRepository.findBookingFilter(id,minDate,maxDate,precio);





        return "redirect:/houses";

    }
}