package com.demo.repository;

import com.demo.model.Booking;
import com.demo.model.StatusBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Listado de las reservas de un usuario ordenadas por Checking
    List<Booking> findByUserBookingOrderByCheckin(Long id);

    // Lista de las reservas de un usuario y de un tipo de estado ordenadas por Checking
    List<Booking> findByUserBookingAndStatusbookingOrderByCheckin(Long userBooking, StatusBooking statusbooking);

    // Lista de las reservas de un usuario de un tipo entre una fecha y otra ordenada por checking
    List<Booking> findByUserBookingAndCheckinBetweenAndStatusbookingOrderByCheckin(Long userBooking, LocalDate checkinAfter, LocalDate checkinBefore, StatusBooking statusbooking);

    // Query para conseguir el historial de Bookings ya visitados y poder recomandarlo a otro usuario.
    @Query(value="""
        SELECT user_house FROM booking WHERE 
            statusbooking='COMPLETED' OR statusbooking='RECOMMENDED' OR statusbooking='RECOMMENDED_BY_OTHER' 
            AND user_booking= ?1
    """,nativeQuery = true)
    List<Booking> toMakePossibleRecommendations(Long id);

    // Mejorado a JPA
    @Query(value="""
        SELECT b.userHouse FROM Booking b WHERE 
            b.statusbooking='COMPLETED' OR b.statusbooking='RECOMMENDED' OR 
            b.statusbooking='RECOMMENDED_BY_OTHER' AND b.userBooking=?1
    """)
    List<Booking> toMakePossibleRecommendationsJPA(Long id);

    // Listado de recomendaciones de otra gente
    @Query(value= """
        SELECT b.userHouse FROM Booking b WHERE 
            b.statusbooking='RECOMMENDE_BY_OTHER' AND b.userBooking=?1
    """)
    List<Booking> recommendationsFromOtherPeople (Long id);

}
