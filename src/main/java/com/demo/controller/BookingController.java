package com.demo.controller;

import com.demo.model.*;
import com.demo.repository.BookingRepository;
import com.demo.repository.HouseRepository;
import com.demo.repository.UserRepository;

import com.demo.service.BookingService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ser.std.DelegatingSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class BookingController {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final HouseRepository houseRepository;
    private final BookingService bookingService;

    public BookingController(BookingRepository bookingRepository,
                             UserRepository userRepository,
                             HouseRepository houseRepository,
                             BookingService bookingService) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.houseRepository = houseRepository;
        this.bookingService = bookingService;
    }


    // BOOKING ID.
    @GetMapping("/booking/{id}")
    public String getBookingById (Model model,@PathVariable Long id)
    {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isPresent()) {

            Booking validBooking = booking.get();

            model.addAttribute("booking", validBooking);
            // A futuro AddOn
            // addons   addonRepository.findByBookingId
            return "host/booking-detail";

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

    // LADO HUESPED

    @GetMapping("/guest/pending/{id}")
    public String listGuestPending (Model model, @PathVariable Long id)
    {

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User validUser = user.get();


            // Reservas del Host
            List<Booking> listBookingGuest = bookingRepository.bookingsGuestPending(id);


            // Atributos de listas pasados al HTML
            model.addAttribute("user",validUser);
            model.addAttribute("listBookingsGuestPending",listBookingGuest);

            return "/guest/booking-list-pending";
        }
        else {
            return "redirect:/index";
        }

    }

    @GetMapping("/guest/cancelled/{id}")
    public String listGuestCancelled (Model model, @PathVariable Long id)
    {

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User validUser = user.get();


            // Reservas del Host
            List<Booking> listBookingGuest = bookingRepository.bookingsGuestCancelled(id);


            // Atributos de listas pasados al HTML
            model.addAttribute("user",validUser);
            model.addAttribute("listBookingsGuestCancelled",listBookingGuest);

            return "/guest/booking-list-cancelled";
        }
        else {
            return "redirect:/index";
        }

    }

    @GetMapping("/guest/completed/{id}")
    public String listGuestCompleted (Model model, @PathVariable Long id)
    {

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User validUser = user.get();


            // Reservas del Host
            List<Booking> listBookingGuest = bookingRepository.bookingsGuestCompleted(id);


            // Atributos de listas pasados al HTML
            model.addAttribute("user",validUser);
            model.addAttribute("listBookingsGuestCompleted",listBookingGuest);

            return "/guest/booking-list-completed";
        }
        else {
            return "redirect:/index";
        }

    }

    @GetMapping("/guest/confirmed/{id}")
    public String listGuestConfirmed (Model model, @PathVariable Long id)
    {

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User validUser = user.get();


            // Reservas del Host
            List<Booking> listBookingGuest = bookingRepository.bookingsGuestConfirmed(id);


            // Atributos de listas pasados al HTML
            model.addAttribute("user",validUser);
            model.addAttribute("listBookingsGuestConfirmed",listBookingGuest);

            return "/guest/booking-list-confirmed";
        }
        else {
            return "redirect:/index";
        }

    }

    // CAMBIO DE ESTADOS HOST

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

            // Ponemos la reserva a completada
            Booking bookingPresent = bookingOptional.get();
            bookingPresent.setStatusbooking(StatusBooking.COMPLETED);
            bookingRepository.save(bookingPresent);

            // Ponemos el apartamento a disponible
            House house = bookingPresent.getUserHouse();
            house.setReserve(StatusReserva.DISPONIBLE);

            User user=bookingPresent.getUserHouse().getHost();

            return "redirect:/host/completed/" + user.getId();

        }

        return "redirect:/houses";

    }

    @GetMapping("/booking/from-pending-to-cancelled-guest/{id}")
    // id del booking
    public String actionFromPendingToCancelledGuest (@PathVariable Long id, Model model) {

        Optional<Booking> bookingOptional =
                bookingRepository.findById(id);
        if (bookingOptional.isPresent()) {
            Booking bookingPresent = bookingOptional.get();
            bookingPresent.setStatusbooking(StatusBooking.CANCELLED);
            bookingRepository.save(bookingPresent);
            User user = bookingPresent.getUserHouse().getHost();
            return "redirect:/guest/cancelled/" + id.toString();
        }
        return "redirect:/houses";
    }


    @PostMapping("booking/update-dates")
    public String updateBooking(
            @RequestParam Long id,
            @RequestParam Long userid,
            @RequestParam LocalDateTime checkin,
            @RequestParam LocalDateTime checkout
    ) {
        Booking booking = bookingRepository.findById(id).orElseThrow();
        booking.setCheckin(checkin);
        booking.setCheckout(checkout);
        if (!bookingService.validateDates(booking))
            return ""; // TODO avisar al usuario de que las fechas están mal

        booking.setStatusbooking(StatusBooking.CONFIRMED);
        User usuario = userRepository.findById(userid).orElseThrow();

        bookingService.recalculateBooking(booking);

        bookingRepository.save(booking);
        //        Enviando al detalle
        return "redirect:/host/confirmed/" + usuario.getId();
        //      Enviar al listado seria ...
        // return "redirect:/restaurantes";
    }

    // El id es la casa seleccionada
    @GetMapping("booking/new/{houseId}")
    public String newBooking(Model model,@PathVariable Long houseId) {

        Optional<House> house = houseRepository.findById(houseId);
        if (house.isPresent()) {

            House houseValid = house.get();

            // Cargamos la reserva vacia
            Booking booking = new Booking();
            booking.setUserHouse(houseValid);

            model.addAttribute("booking", booking);
            // Cargamos los tipos de reserva permitidos
            //model.addAttribute("tiposreserva", StatusBooking.values());
            // Cargamos todos los usuarios que son posibles huespedes.
            model.addAttribute("usuarios", userRepository.findAll());

            return "host/booking-form";
        }

        // Utilizar un @RequestParam para saber el usuario al que volver
        return "redirect:/houses";
    }

    // Guarda el formulario de Reservas
    @PostMapping("booking")
    public String createBooking (@ModelAttribute Booking booking, @AuthenticationPrincipal User user) {

        // Calculos de noches y precios
        booking.setNumberNights(booking.calculateNights(booking.getEstimatedCheckin(),booking.getEstimatedCheckout()));
        booking.setTotalPrice(booking.calculateTotalPrice(booking.getNumberNights()));

        if (!bookingService.validateDates(booking)){
            // model.addatribute  error "Fechas incorrectas"
            return "";
        }

        if (user != null && user.getRole() == Role.ROLE_USER) {
            // Si el usuario no es admin, entonces asigno el User user cargado por Spring Security
            // para que no nos asignen una reserva a otro usuario diferente y evitar problemas de seguridad.
            // Si eres ROLE_ADMIN no entra en este if y sí permite que asocie el usuario que llega de formulario
            booking.setUserBooking(user);
        }
        bookingRepository.save(booking);

        // Al hacer el cambio dee estado se pierde el "HOST_ID" de la "HOUSE"
        Long idHouseModificada = booking.getUserHouse().getId();

        // Esta casa debe de estar como reservada
        Optional<House> casaParaReservar = houseRepository.findById(idHouseModificada);
        if (casaParaReservar.isPresent()) {
            House casaParaReservarValid = casaParaReservar.get();
            casaParaReservarValid.setReserve(StatusReserva.RESERVADA);
            houseRepository.save(casaParaReservarValid);
        }

        return "redirect:/booking/" + booking.getId();

    }


}
