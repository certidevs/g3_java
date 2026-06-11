package com.demo.repository;

import com.demo.model.Booking;
import com.demo.model.House;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {


    // FILTRO PARA PANTALLAS HOST
    //////////////////////////////

    // FILTRO PARA PANTALLA DE RESERVAS "PENDING"

    @Query("""
        SELECT bk FROM Booking bk WHERE bk.userHouse.host.id=?1 AND bk.statusbooking='PENDING'
    """)
    List<Booking> bookingsHostPending(Long id);

    // FILTRO PARA PANTALLA DE RESERVAS "CONFIRMED"

    @Query("""
        SELECT bk FROM Booking bk WHERE bk.userHouse.host.id=?1 AND bk.statusbooking='CONFIRMED'
    """)
    List<Booking> bookingsHostConfirmed(Long id);

    // FILTRO PARA PANTALLA DE RESERVAS "CANCELLED"
    @Query("""
        SELECT bk FROM Booking bk WHERE bk.userHouse.host.id=?1 AND bk.statusbooking='CANCELLED'
    """)
    List<Booking> bookingsHostCancelled(Long id);

    // FILTRO PARA PANTALLA DE RESERVAS "COMPLETED"
    @Query("""
        SELECT bk FROM Booking bk WHERE bk.userHouse.host.id=?1 AND bk.statusbooking='COMPLETED'
    """)
    List<Booking> bookingsHostCompleted(Long id);

    // FILTRO PARA PANTALLAS GUEST
    //////////////////////////////
    @Query("""
        SELECT bk FROM Booking bk WHERE bk.userBooking.id=?1 AND bk.statusbooking='PENDING'
    """)
    List<Booking> bookingsGuestPending(Long id);

    @Query("""
        SELECT bk FROM Booking bk WHERE bk.userBooking.id=?1 AND bk.statusbooking='CANCELLED'
    """)
    List<Booking> bookingsGuestCancelled(Long id);

    @Query("""
        SELECT bk FROM Booking bk WHERE bk.userBooking.id=?1 AND bk.statusbooking='CONFIRMED'
    """)
    List<Booking> bookingsGuestConfirmed(Long id);

    @Query("""
        SELECT bk FROM Booking bk WHERE bk.userBooking.id=?1 AND bk.statusbooking='COMPLETED'
    """)
    List<Booking> bookingsGuestCompleted(Long id);

    // BookingFilterRepository + Querys Sin filtro:

    // Casas alquiladas por un (guest)
    @Query("""
        SELECT bk.userHouse FROM Booking bk WHERE bk.userBooking.id=:id
         AND (:price IS NULL OR :price>bk.userHouse.pricePerNight)
    """)
    List<House> housesBookingGuest(@Param("id") Long id, @Param("price") Double price);

    // Casas alquiladas por un anfitrion (host)
    @Query("""
        SELECT hs FROM House hs WHERE hs.host.id=:id
          AND (:price IS NULL OR :price>hs.pricePerNight)
    """)
    List<House> houseBookingHost(@Param("id") Long id, @Param("price") Double price);

    @Query("""
        SELECT bk FROM Booking bk WHERE bk.userBooking.id=:id AND bk.statusbooking<>'COMPLETED'
            AND (:price IS NULL OR :price>bk.userHouse.pricePerNight)
            AND (
                (:searchDate IS NULL OR (:searchDate>=bk.estimatedCheckin AND :searchDate<=bk.estimatedCheckout)) OR
                (:searchDate IS NULL OR (:searchDate>=bk.checkin AND :searchDate<=bk.checkout))
            )
    """)
    List<Booking> bookingsGuest(@Param("id") Long id, @Param("searchDate") LocalDateTime searchDate, @Param("price") Double price);

    @Query("""
                SELECT bk FROM Booking bk WHERE bk.userHouse.host.id=:id
                    AND (:price IS NULL OR :price>bk.userHouse.pricePerNight)
                    AND (
                        (:searchDate IS NULL OR (:searchDate>=bk.estimatedCheckin AND :searchDate<=bk.estimatedCheckout)) OR
                        (:searchDate IS NULL OR (:searchDate>=bk.checkin AND :searchDate<=bk.checkout))
                    )
            """)
    List<Booking> bookingsHost(@Param("id") Long id, @Param("searchDate") LocalDateTime searchDate, @Param("price") Double price);

}
