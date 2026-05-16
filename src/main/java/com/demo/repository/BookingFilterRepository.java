package com.demo.repository;

import com.demo.model.Booking;
import com.demo.model.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingFilterRepository extends JpaRepository<Booking, Long> {

    // Casas alquiladas por un (guest)
    @Query("""
        SELECT hs FROM House hs WHERE hs.host.id=:idUsuario  
            AND hs.pricePerNight<:price               
    """)
    List<House> bookingsFilterHouseHost(
            @Param("idUsuario") Long id,
            @Param("minDate") LocalDate minDate,
            @Param("maxDate") LocalDate maxDate,
            @Param("price") Double price);

/*
    // Casas alquiladas por un anfitrion (host)
    @Query("""
        SELECT hs FROM House hs WHERE hs.host.id=?1
    """)
    List<House> houseBookingHost(Long id);

    @Query("""
        SELECT bk FROM Booking bk WHERE bk.userBooking.id=?1 AND bk.statusbooking<>'COMPLETED'
    """)
    List<Booking> bookingsGuest(Long id);

    @Query("""
        SELECT bk FROM Booking bk WHERE bk.userHouse.host.id=?1
    """)
    List<Booking> bookingsHost(Long id);
*/
}
