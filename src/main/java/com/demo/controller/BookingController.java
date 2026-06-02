package com.demo.controller;

import com.demo.model.*;
import com.demo.repository.BookingRepository;
import com.demo.repository.HouseRepository;

import com.demo.service.BookingService;
import com.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class BookingController {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final HouseRepository houseRepository;
    private final BookingService bookingService;

    // BOOKING ID.
    @GetMapping("/booking/{id}")
    public String getBookingById(Model model, @PathVariable Long id) {
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

    @GetMapping("/booking/edit/{id}")
    public String editBooking(Model model, @PathVariable Long id) {

        model.addAttribute("booking", bookingRepository.findById(id).orElseThrow());
        model.addAttribute("estados", StatusBooking.values());
        return "host/booking-form-update";
    }


    // LADO ANFITRION
    @GetMapping("/host/pending/{id}")
    public String listHostPending(Model model, @PathVariable Long id) {

        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            User validUser = user.get();


            // Reservas del Host
            List<Booking> listBookingHost = bookingRepository.bookingsHostPending(id);


            // Atributos de listas pasados al HTML
            model.addAttribute("user", validUser);
            model.addAttribute("listBookingsHostPending", listBookingHost);

            return "/host/booking-list-pending-host";
        } else {
            return "redirect:/index";
        }

    }

    @GetMapping("/host/confirmed/{id}")
    public String listHostConfirmed(Model model, @PathVariable Long id) {

        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            User validUser = user.get();


            // Reservas del Host
            List<Booking> listBookingHost = bookingRepository.bookingsHostConfirmed(id);


            // Atributos de listas pasados al HTML
            model.addAttribute("user", validUser);
            model.addAttribute("listBookingsHostConfirmed", listBookingHost);

            return "/host/booking-list-confirmed-host";
        } else {
            return "redirect:/index";
        }

    }

    @GetMapping("/host/cancelled/{id}")
    public String listHostCancelled(Model model, @PathVariable Long id) {

        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            User validUser = user.get();


            // Reservas del Host
            List<Booking> listBookingHost = bookingRepository.bookingsHostCancelled(id);


            // Atributos de listas pasados al HTML
            model.addAttribute("user", validUser);
            model.addAttribute("listBookingsHostCancelled", listBookingHost);

            return "/host/booking-list-cancelled-host";
        } else {
            return "redirect:/index";
        }

    }

    @GetMapping("/host/completed/{id}")
    public String listHostCompleted(Model model, @PathVariable Long id) {

        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            User validUser = user.get();


            // Reservas del Host
            List<Booking> listBookingHost = bookingRepository.bookingsHostCompleted(id);


            // Atributos de listas pasados al HTML
            model.addAttribute("user", validUser);
            model.addAttribute("listBookingsHostCompleted", listBookingHost);

            return "/host/booking-list-completed-host";
        } else {
            return "redirect:/index";
        }

    }

    // LADO HUESPED

    @GetMapping("/guest/pending/{id}")
    public String listGuestPending(Model model, @PathVariable Long id) {

        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            User validUser = user.get();


            // Reservas del Host
            List<Booking> listBookingGuest = bookingRepository.bookingsGuestPending(id);


            // Atributos de listas pasados al HTML
            model.addAttribute("user", validUser);
            model.addAttribute("listBookingsGuestPending", listBookingGuest);

            return "/guest/booking-list-pending";
        } else {
            return "redirect:/index";
        }

    }

    @GetMapping("/guest/cancelled/{id}")
    public String listGuestCancelled(Model model, @PathVariable Long id) {

        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            User validUser = user.get();


            // Reservas del Host
            List<Booking> listBookingGuest = bookingRepository.bookingsGuestCancelled(id);


            // Atributos de listas pasados al HTML
            model.addAttribute("user", validUser);
            model.addAttribute("listBookingsGuestCancelled", listBookingGuest);

            return "/guest/booking-list-cancelled";
        } else {
            return "redirect:/index";
        }

    }

    @GetMapping("/guest/completed/{id}")
    public String listGuestCompleted(Model model, @PathVariable Long id) {

        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            User validUser = user.get();


            // Reservas del Host
            List<Booking> listBookingGuest = bookingRepository.bookingsGuestCompleted(id);


            // Atributos de listas pasados al HTML
            model.addAttribute("user", validUser);
            model.addAttribute("listBookingsGuestCompleted", listBookingGuest);

            return "/guest/booking-list-completed";
        } else {
            return "redirect:/index";
        }

    }

    @GetMapping("/guest/confirmed/{id}")
    public String listGuestConfirmed(Model model, @PathVariable Long id) {

        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            User validUser = user.get();


            // Reservas del Host
            List<Booking> listBookingGuest = bookingRepository.bookingsGuestConfirmed(id);


            // Atributos de listas pasados al HTML
            model.addAttribute("user", validUser);
            model.addAttribute("listBookingsGuestConfirmed", listBookingGuest);

            return "/guest/booking-list-confirmed";
        } else {
            return "redirect:/index";
        }

    }

    // CAMBIO DE ESTADOS HOST

    @GetMapping("/booking/from-pending-to-confirmed/{id}")
    // id del booking
    public String actionFromPendingToConfirmed(@PathVariable Long id, Model model) {

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
    public String actionFromPendingToCancelled(@PathVariable Long id, Model model) {

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
    public String actionFromConfirmedToCancelled(@PathVariable Long id, Model model) {

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
    public String actionFromConfirmedToCompleted(@PathVariable Long id, Model model) {

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

            User user = bookingPresent.getUserHouse().getHost();

            return "redirect:/host/completed/" + user.getId();

        }

        return "redirect:/houses";

    }

    @GetMapping("/booking/from-pending-to-cancelled-guest/{id}")
    // id del booking
    public String actionFromPendingToCancelledGuest(@PathVariable Long id, Model model) {

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
        User usuario = userService.findById(userid).orElseThrow();

        bookingService.recalculateBooking(booking);

        bookingRepository.save(booking);
        //        Enviando al detalle
        return "redirect:/host/confirmed/" + usuario.getId();
        //      Enviar al listado seria ...
        // return "redirect:/restaurantes";
    }

    // El id es la casa seleccionada
    @GetMapping("booking/new/{houseId}")
    public String newBooking(Model model, @PathVariable Long houseId) {

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
            model.addAttribute("usuarios", userService.findAll());

            return "host/booking-form";
        }

        // Utilizar un @RequestParam para saber el usuario al que volver
        return "redirect:/houses";
    }

    // Guarda el formulario de Reservas (sirve para ALTA y EDICION; ambos formularios hacen POST /booking)
    @PostMapping("booking")
    public String createBooking(@ModelAttribute Booking booking, @AuthenticationPrincipal User user) {

        // 1) La casa SIEMPRE se recarga gestionada desde BD a partir de su id.
        //    El formulario solo envia *{userHouse.id}; nunca confiamos en el objeto House que
        //    construye el binding (seria una entidad transitoria sin id -> TransientObjectException al guardar).
        Long houseId = (booking.getUserHouse() != null) ? booking.getUserHouse().getId() : null;
        if (houseId == null) {
            return "redirect:/houses";
        }
        Optional<House> houseOpt = houseRepository.findById(houseId);
        if (houseOpt.isEmpty()) {
            return "redirect:/houses";
        }
        House house = houseOpt.get();

        // 2) Distinguimos ALTA de EDICION segun llegue id de booking.
        Booking toSave;
        if (booking.getId() != null) {
            // EDICION: partimos del booking gestionado y copiamos SOLO los campos editables.
            // Asi conservamos el userBooking original (no se pierde al editar como admin) y hacemos UPDATE real.
            toSave = bookingRepository.findById(booking.getId()).orElseThrow();
            toSave.setEstimatedCheckin(booking.getEstimatedCheckin());
            toSave.setEstimatedCheckout(booking.getEstimatedCheckout());
            toSave.setCheckin(booking.getCheckin());
            toSave.setCheckout(booking.getCheckout());
            toSave.setStatusbooking(booking.getStatusbooking());
        } else {
            // ALTA nueva.
            toSave = booking;
            if (user != null && user.getRole() == Role.ROLE_USER) {
                // Si no es admin, el huesped es el usuario logueado: evitamos que asignen la reserva a otro.
                // Si es ROLE_ADMIN se respeta el userBooking que llegue del formulario (selector de usuario).
                toSave.setUserBooking(user);
            }
        }
        toSave.setUserHouse(house);

        // 3) Validacion de fechas estimadas.
        if (!bookingService.validateDates(toSave)) {
            // TODO: avisar al usuario con un mensaje de error "Fechas incorrectas"
            return "redirect:/houses";
        }

        // 4) Calculo de noches y precio (usa la casa gestionada -> pricePerNight valido).
        toSave.setNumberNights(toSave.calculateNights(toSave.getEstimatedCheckin(), toSave.getEstimatedCheckout()));
        toSave.setTotalPrice(toSave.calculateTotalPrice(toSave.getNumberNights()));

        bookingRepository.save(toSave);

        // 5) Sincronizamos el estado de la casa con el estado de la reserva.
        if (toSave.getStatusbooking() == StatusBooking.CANCELLED
                || toSave.getStatusbooking() == StatusBooking.COMPLETED) {
            house.setReserve(StatusReserva.DISPONIBLE);
        } else {
            house.setReserve(StatusReserva.RESERVADA);
        }
        houseRepository.save(house);

        return "redirect:/booking/" + toSave.getId();

    }


}
