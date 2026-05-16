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

        return true;
    }

    public void recalculateBooking(Booking booking){

        // Actualizar noches y precio
        booking.setNumberNights(booking.calculateNights(booking.getCheckin(),booking.getCheckout()));
        booking.setTotalPrice(booking.calculateTotalPrice(booking.getNumberNights()));

    }

}
