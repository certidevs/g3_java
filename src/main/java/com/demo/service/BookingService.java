package com.demo.service;

import com.demo.model.Booking;
import com.demo.model.House;
import com.demo.repository.BookingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class BookingService {

    public final BookingRepository bookingRepository;

    // validateDates
    public boolean validateDates(Booking booking){
        var checkin = booking.getEstimatedCheckin();
        var checkout = booking.getEstimatedCheckout();

        if (checkin == null || checkout == null)
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

    // API de uso de bookingRepository, con null en parámetros opcionales gracias a las query internas del mismo repository
    public List<Booking> getHostBookings(Long hostId, LocalDateTime searchDate, Double maxPrice) {
        return bookingRepository.bookingsHost(hostId, searchDate, maxPrice);
    }

    public List<Booking> getHostBookings(Long hostId) {
        return getHostBookings(hostId, null, null);
    }

    public List<Booking> getGuestBookings(Long guestId, LocalDateTime searchDate, Double maxPrice) {
        return bookingRepository.bookingsGuest(guestId, searchDate, maxPrice);
    }

    public List<Booking> getGuestBookings(Long guestId) {
        return getGuestBookings(guestId, null, null);
    }

    public List<House> getHostProperties(Long hostId, Double maxPrice) {
        return bookingRepository.houseBookingHost(hostId, maxPrice);
    }

    public List<House> getHostProperties(Long hostId) {
        return getHostProperties(hostId, null);
    }

    public List<House> getGuestProperties(Long guestId, Double maxPrice) {
        return bookingRepository.housesBookingGuest(guestId, maxPrice);
    }

    public List<House> getGuestProperties(Long guestId) {
        return getGuestProperties(guestId, null);
    }

}
