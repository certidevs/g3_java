package com.demo.repository;

import com.demo.model.Booking;
import com.demo.model.StatusBooking;
import com.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // LADO DEL ANFITRION HOST.
    /////////////////////////

    // Listado de las reservas pendientes del lado del anfitrion
    @Query("""
        SELECT bk FROM Booking bk WHERE bk.statusbooking='PENDING' AND 
            bk.userHouse.host.id=?1 ORDER BY bk.checkin 
    """)
    List<Booking> findPendingHost(Long id);

    // Listado de las reservas confirmadas para cancelar,checkin,checkout
    @Query("""
        SELECT bk FROM Booking bk WHERE bk.statusbooking='CONFIRMED' AND 
            bk.userHouse.host.id=?1 ORDER BY bk.checkin
    """)
    List<Booking> findConfirmedHost(Long id);

    // LADO DE HUESPED
    //////////////////

    // Lista de las reservas de un usuario pendientes
    @Query ("""
        SELECT bk FROM Booking bk WHERE bk.userHouse.id=?1  
                 AND bk.statusbooking='PENDING' ORDER BY bk.checkin DESC
    """)
    List<Booking> findPendings(Long id);

    // Lista de las reservas de un usuario confirmadas
    @Query ("""
        SELECT bk FROM Booking bk WHERE bk.userHouse.id=?1  
                 AND bk.statusbooking='CONFIRMED' ORDER BY bk.checkin DESC
    """)
    List<Booking> findConfirmed(Long id);

    // Lista de cancelaciones de un huesped
    @Query("""
        SELECT bk FROM Booking bk WHERE bk.userHouse.id=?1
            AND bk.statusbooking='CANCELLED' ORDER BY bk.checkin DESC
    """)
    List<Booking> findCancelled(Long id);

    ///////////////////////////////////////////////////
    ///
    /// AMPLIACIONES

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
