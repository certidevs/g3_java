package com.demo.service;

import com.demo.model.Booking;
import com.demo.repository.BookingRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class BookingService {

    private BookingRepository bookingRepository;

    // validateDates
    public boolean validateDates(Booking booking){
        var checkin = booking.getCheckin();
        var checkout = booking.getCheckout();

        if (checkin == null && checkout == null)
            return false;

        if (checkin.isAfter(checkout))
            return false;

        // no se puede reservar antes de hoy
        if (checkin.isBefore(LocalDateTime.now()))
            return false;

//        if booking.getUserHouse().getMinimumNights

        return true;
    }

    public Booking recalculateBooking(Booking booking){
        // precondiciones

        // calcular precio

        // opcional: guardar en base datos
        // bookingRepository.save(booking);

        return booking;
    }

}
