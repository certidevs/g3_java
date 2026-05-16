package com.demo.repository;

import com.demo.model.Booking;
import com.demo.model.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingFilterRepository extends JpaRepository<Booking, Long> {

    // Casas alquiladas por un (guest)
    @Query("""
        SELECT hs FROM House hs WHERE hs.host.id=:idUsuario  
          AND (:price IS NULL OR :price<hs.pricePerNight) 
    """)
    List<House> houseBookinHostFilter(
                    @Param("idUsuario") Long id,
                    @Param("price") Double price);

    @Query("""
        SELECT bk.userHouse  FROM Booking bk WHERE bk.userBooking.id=:idUsuario
         AND (:price IS NULL OR :price<bk.userHouse.pricePerNight)    
    """)
    List<House> houseBookingGuestFilter(
                         @Param("idUsuario") Long id,
                         @Param("price") Double price);

    @Query("""
        SELECT bk FROM Booking bk WHERE bk.userHouse.host.id=:idUsuario 
            AND (:price IS NULL OR :price<bk.userHouse.pricePerNight) 
            AND (
                (:searchDate IS NULL OR (:searchDate>=bk.estimatedCheckin AND :searchDate<=bk.estimatedCheckout)) OR
                (:searchDate IS NULL OR (:searchDate>=bk.checkin AND :searchDate<=bk.checkout))    
            )        
    """)
    List<Booking> bookingsHostFilter(
                    @Param("idUsuario")Long id,
                    @Param("searchDate") LocalDateTime searchDate,
                    @Param("price") Double price);

    @Query("""
        SELECT bk FROM Booking bk WHERE bk.userBooking.id=:idUsuario 
            AND (:price IS NULL OR :price<bk.userHouse.pricePerNight) 
            AND (
                (:searchDate IS NULL OR (:searchDate>=bk.estimatedCheckin AND :searchDate<=bk.estimatedCheckout)) OR
                (:searchDate IS NULL OR (:searchDate>=bk.checkin AND :searchDate<=bk.checkout))    
            )        
    """)
    List<Booking> bookingsGuestFilter(
            @Param("idUsuario")Long id,
            @Param("searchDate") LocalDateTime searchDate,
            @Param("price") Double price);

}
