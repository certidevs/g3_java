package com.demo.controller;

import com.demo.model.*;
import com.demo.model.enums.StatusBooking;
import com.demo.model.enums.StatusReserva;
import com.demo.repository.HouseRepository;
import com.demo.service.BookingService;
import com.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class BookingController {

    private final UserService userService;
    private final HouseRepository houseRepository;
    private final BookingService bookingService;

    @GetMapping("/booking/{id}")
    public String getBookingById(Model model, @PathVariable Long id) {
        Optional<Booking> booking = bookingService.findById(id);
        if (booking.isPresent()) {
            Booking validBooking = booking.get();
            model.addAttribute("booking", validBooking);
            return "booking/booking-detail";
        }
        return "redirect:/index";
    }

    @GetMapping("/booking/edit/{id}")
    public String editBooking(Model model, @PathVariable Long id) {
        model.addAttribute("booking", bookingService.findById(id).orElseThrow());
        model.addAttribute("estados", StatusBooking.values());
        return "booking/booking-form";
    }

    @PostMapping("booking/{id}/finish")
    public String finish(
            @PathVariable Long id,
            @RequestParam(required = false) String cardOwner,
            @RequestParam(required = false) String cardNumber,
            @RequestParam(required = false) String cardExpirationDate,
            @RequestParam(required = false) String cardSecretCode,
            RedirectAttributes redirectAttributes
    ) {
        Booking booking = bookingService.findById(id).orElseThrow();
        String number = cardNumber == null ? "" : cardNumber.replace("\\s", "");
        if (!number.matches("\\d{16}")) {
            redirectAttributes.addFlashAttribute("error", "Invalid card number");
            return "redirect:/booking/" + id;
        }
        if (cardExpirationDate == null || !cardExpirationDate.matches("\\d{2}/\\d{2}")) {
            redirectAttributes.addFlashAttribute("error", "La caducidad debe tener formato MM/YY");
            return "redirect:/booking/" + id;
        }
        if (cardSecretCode == null || !cardSecretCode.matches("\\d{3}")) {
            redirectAttributes.addFlashAttribute("error", "Invalid card secret code");
            return "redirect:/booking/" + id;
        }
        if (cardOwner == null || cardOwner.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Card owner is required");
            return "redirect:/booking/" + id;
        }

        booking.setCardNumber(cardNumber);
        booking.setCardOwner(cardOwner);
        booking.setCardExpirationDate(cardExpirationDate);
        booking.setStatusbooking(StatusBooking.CONFIRMED);
        booking.setCheckin(booking.getEstimatedCheckin());
        booking.setCheckout(booking.getEstimatedCheckout());

        bookingService.save(booking);

        redirectAttributes.addFlashAttribute("message", "Pedido finalizado correctamente");
        return "redirect:/booking/" + id;
    }



    // LADO ANFITRION
    @GetMapping("/host/pending/{id}")
    public String listHostPending(Model model, @PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            User validUser = user.get();
            List<Booking> listBookingHost = bookingService.getHostBookings(id, StatusBooking.PENDING);
            model.addAttribute("user", validUser);
            model.addAttribute("bookings", listBookingHost);
            model.addAttribute("viewRole", "HOST");
            model.addAttribute("viewTitle", "Reservas pendientes");
            return "booking/booking-list";
        } else {
            return "redirect:/index";
        }
    }

    @GetMapping("/host/confirmed/{id}")
    public String listHostConfirmed(Model model, @PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            User validUser = user.get();
            List<Booking> listBookingHost = bookingService.getHostBookings(id, StatusBooking.CONFIRMED);
            model.addAttribute("user", validUser);
            model.addAttribute("bookings", listBookingHost);
            model.addAttribute("viewRole", "HOST");
            model.addAttribute("viewTitle", "Reservas confirmadas");
            return "booking/booking-list";
        } else {
            return "redirect:/index";
        }
    }

    @GetMapping("/host/cancelled/{id}")
    public String listHostCancelled(Model model, @PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            User validUser = user.get();
            List<Booking> listBookingHost = bookingService.getHostBookings(id, StatusBooking.CANCELLED);
            model.addAttribute("user", validUser);
            model.addAttribute("bookings", listBookingHost);
            model.addAttribute("viewRole", "HOST");
            model.addAttribute("viewTitle", "Reservas canceladas");
            return "booking/booking-list";
        } else {
            return "redirect:/index";
        }
    }

    @GetMapping("/host/completed/{id}")
    public String listHostCompleted(Model model, @PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            User validUser = user.get();
            List<Booking> listBookingHost = bookingService.getHostBookings(id, StatusBooking.COMPLETED);
            model.addAttribute("user", validUser);
            model.addAttribute("bookings", listBookingHost);
            model.addAttribute("viewRole", "HOST");
            model.addAttribute("viewTitle", "Reservas completadas");
            return "booking/booking-list";
        } else {
            return "redirect:/index";
        }
    }

    @GetMapping("/guest/pending/{id}")
    public String listGuestPending(Model model, @PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            User validUser = user.get();
            List<Booking> listBookingGuest = bookingService.getGuestBookings(id, StatusBooking.PENDING);
            model.addAttribute("user", validUser);
            model.addAttribute("bookings", listBookingGuest);
            model.addAttribute("viewRole", "GUEST");
            model.addAttribute("viewTitle", "Reservas pendientes");
            return "booking/booking-list";
        } else {
            return "redirect:/index";
        }
    }

    @GetMapping("/guest/cancelled/{id}")
    public String listGuestCancelled(Model model, @PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            User validUser = user.get();
            List<Booking> listBookingGuest = bookingService.getGuestBookings(id, StatusBooking.CANCELLED);
            model.addAttribute("user", validUser);
            model.addAttribute("bookings", listBookingGuest);
            model.addAttribute("viewRole", "GUEST");
            model.addAttribute("viewTitle", "Reservas canceladas");
            return "booking/booking-list";
        } else {
            return "redirect:/index";
        }
    }

    @GetMapping("/guest/completed/{id}")
    public String listGuestCompleted(Model model, @PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            User validUser = user.get();
            List<Booking> listBookingGuest = bookingService.getGuestBookings(id, StatusBooking.COMPLETED);
            model.addAttribute("user", validUser);
            model.addAttribute("bookings", listBookingGuest);
            model.addAttribute("viewRole", "GUEST");
            model.addAttribute("viewTitle", "Reservas completadas");
            return "booking/booking-list";
        } else {
            return "redirect:/index";
        }
    }

    @GetMapping("/guest/confirmed/{id}")
    public String listGuestConfirmed(Model model, @PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            User validUser = user.get();
            List<Booking> listBookingGuest = bookingService.getGuestBookings(id, StatusBooking.CONFIRMED);
            model.addAttribute("user", validUser);
            model.addAttribute("bookings", listBookingGuest);
            model.addAttribute("viewRole", "GUEST");
            model.addAttribute("viewTitle", "Reservas confirmadas");
            return "booking/booking-list";
        } else {
            return "redirect:/index";
        }
    }

    @GetMapping("/booking/from-pending-to-confirmed/{id}")
    public String actionFromPendingToConfirmed(@PathVariable Long id, Model model) {
        Optional<Booking> bookingOptional = bookingService.findById(id);
        if (bookingOptional.isPresent()) {
            Booking bookingPresent = bookingOptional.get();
            bookingPresent.setStatusbooking(StatusBooking.CONFIRMED);
            bookingService.save(bookingPresent);
            User user = bookingPresent.getUserHouse().getHost();
            return "redirect:/host/pending/" + user.getId();
        }
        return "redirect:/houses";
    }

    @GetMapping("/booking/from-pending-to-cancelled/{id}")
    public String actionFromPendingToCancelled(@PathVariable Long id, Model model) {
        Optional<Booking> bookingOptional = bookingService.findById(id);
        if (bookingOptional.isPresent()) {
            Booking bookingPresent = bookingOptional.get();
            bookingPresent.setStatusbooking(StatusBooking.CANCELLED);
            bookingService.save(bookingPresent);
            User user = bookingPresent.getUserHouse().getHost();
            return "redirect:/host/cancelled/" + user.getId();
        }
        return "redirect:/houses";
    }

    @GetMapping("/booking/from-confirmed-to-cancelled/{id}")
    public String actionFromConfirmedToCancelled(@PathVariable Long id, Model model) {
        Optional<Booking> bookingOptional = bookingService.findById(id);
        if (bookingOptional.isPresent()) {
            Booking bookingPresent = bookingOptional.get();
            bookingPresent.setStatusbooking(StatusBooking.CANCELLED);
            bookingService.save(bookingPresent);
            User user = bookingPresent.getUserHouse().getHost();
            return "redirect:/host/cancelled/" + user.getId();
        }
        return "redirect:/houses";
    }

    @GetMapping("/booking/from-confirmed-to-completed/{id}")
    public String actionFromConfirmedToCompleted(@PathVariable Long id, Model model) {
        Optional<Booking> bookingOptional = bookingService.findById(id);
        if (bookingOptional.isPresent()) {
            Booking bookingPresent = bookingOptional.get();
            bookingPresent.setStatusbooking(StatusBooking.COMPLETED);
            bookingService.save(bookingPresent);
            House house = bookingPresent.getUserHouse();
            house.setReserve(StatusReserva.DISPONIBLE);
            User user = bookingPresent.getUserHouse().getHost();
            return "redirect:/host/completed/" + user.getId();
        }
        return "redirect:/houses";
    }

    @GetMapping("/booking/from-pending-to-cancelled-guest/{id}")
    public String actionFromPendingToCancelledGuest(@PathVariable Long id, Model model) {
        Optional<Booking> bookingOptional = bookingService.findById(id);
        if (bookingOptional.isPresent()) {
            Booking bookingPresent = bookingOptional.get();
            bookingPresent.setStatusbooking(StatusBooking.CANCELLED);
            bookingService.save(bookingPresent);
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
        Booking booking = bookingService.findById(id).orElseThrow();
        booking.setCheckin(checkin);
        booking.setCheckout(checkout);
        if (!bookingService.validateDates(booking))
            return "";

        booking.setStatusbooking(StatusBooking.CONFIRMED);
        User usuario = userService.findById(userid).orElseThrow();

        bookingService.recalculateBooking(booking);

        bookingService.save(booking);
        return "redirect:/host/confirmed/" + usuario.getId();
    }

    @GetMapping("booking/new/{houseId}")
    public String newBooking(Model model, @PathVariable Long houseId) {
        Optional<House> house = houseRepository.findById(houseId);
        if (house.isPresent()) {
            House houseValid = house.get();
            Booking booking = new Booking();
            booking.setUserHouse(houseValid);
            model.addAttribute("booking", booking);
            model.addAttribute("usuarios", userService.findAll());
            return "booking/booking-form";
        }
        return "redirect:/houses";
    }

    @PostMapping("booking")
    public String createBooking(@ModelAttribute Booking booking, @AuthenticationPrincipal User user) {
        Long houseId = (booking.getUserHouse() != null) ? booking.getUserHouse().getId() : null;
        if (houseId == null) {
            return "redirect:/houses";
        }
        Optional<House> houseOpt = houseRepository.findById(houseId);
        if (houseOpt.isEmpty()) {
            return "redirect:/houses";
        }
        House house = houseOpt.get();

        Booking toSave;
        if (booking.getId() != null) {
            toSave = bookingService.findById(booking.getId()).orElseThrow();
            toSave.setEstimatedCheckin(booking.getEstimatedCheckin());
            toSave.setEstimatedCheckout(booking.getEstimatedCheckout());
            toSave.setCheckin(booking.getCheckin());
            toSave.setCheckout(booking.getCheckout());
            toSave.setStatusbooking(booking.getStatusbooking());
        } else {
            toSave = booking;
            if (user != null) {
                toSave.setUserBooking(user);
            }
        }
        toSave.setUserHouse(house);

        if (!bookingService.validateDates(toSave)) {
            return "redirect:/houses";
        }

        if ((toSave.getStatusbooking() == StatusBooking.PENDING)
                || (toSave.getStatusbooking() == StatusBooking.CANCELLED)) {
            toSave.setNumberNights(toSave.calculateNights(toSave.getEstimatedCheckin(), toSave.getEstimatedCheckout()));
            toSave.setTotalPrice(toSave.calculateTotalPrice(toSave.getNumberNights()));
        }
        if ((toSave.getStatusbooking() == StatusBooking.CONFIRMED)
                || (toSave.getStatusbooking() == StatusBooking.COMPLETED)) {
            toSave.setNumberNights(toSave.calculateNights(toSave.getCheckin(), toSave.getCheckout()));
            toSave.setTotalPrice(toSave.calculateTotalPrice(toSave.getNumberNights()));
        }

        bookingService.save(toSave);

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
