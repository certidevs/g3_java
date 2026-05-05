package com.demo.repository;

import com.demo.model.Booking;
import com.demo.model.House;
import com.demo.model.StatusBooking;
import com.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // FILTROS GENERALES PARA EL PANEL DE CONTROL

    // Casas alquiladas por un (guest)
    @Query("""
        SELECT bk.userHouse FROM Booking bk WHERE bk.userBooking.id=?1 
    """)
    List<House> housesBookingGuest(Long id);

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

    // FILTRO PARA PANTALLA DE RESERVAS "PENDING"


    @Query("""
        SELECT bk FROM Booking bk WHERE bk.userHouse.host.id=?1 AND bk.statusbooking='PENDING' 
    """)
    List<Booking> bookingsHostPending(Long id);


    // TODO . A PARTIR DE AQUI

    // FILTRO PARA PANTALLA DE RESERVAS "CONFIRMED"

    @Query(value = """
        SELECT * FROM BOOKINGS WHERE USER_HOUSE_ID=?1 AND STATUSBOOKING='CONFIRMED'
    """,nativeQuery = true)
    List<Booking> bookingsHostConfirmed(Long id);

    // FILTRO PARA PANTALLA DE RESERVAS "CANCELLED"
    @Query(value = """
        SELECT * FROM BOOKINGS WHERE USER_HOUSE_ID=?1 AND STATUSBOOKING='CANCELLED'
    """,nativeQuery = true)
    List<Booking> bookingsHostCancelled(Long id);


}
