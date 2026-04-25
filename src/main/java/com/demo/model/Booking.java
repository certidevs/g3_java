package com.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ManyToOne cuando haya Usuarios
    // A MEJORAR
    @ManyToOne
    @JoinColumn(nullable = false)
    private User userBooking;

    // Tiempo de chequeo estimado
    private LocalDateTime estimatedCheckin;
    private LocalDateTime estimatedCheckout;

    private Long numberNights;

    // Tiempos de chequeos confirmados
    private LocalDateTime checkin;
    private LocalDateTime checkout;

    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private StatusBooking statusbooking = StatusBooking.PENDING;

    // ManyToOne a House
     // A MEJORAR
    @ManyToOne
    @JoinColumn(nullable = false)
    private House userHouse;

    private Long calculateNights (LocalDateTime tiempoin,LocalDateTime tiempoout)
    {
        // Calcular noches
        Long noches = ChronoUnit.DAYS.between(tiempoin,tiempoout);
        return  noches;

    }

    // Constructor para una nueva peticion de alquiler.
    // Pone el estado en PENDING
    public Booking(User userBooking,House userHouse,
                   LocalDateTime estimatedCheckin,LocalDateTime estimatedCheckout)
    {
        // Calculamos en nro. de noches
        Long nroNights = calculateNights(estimatedCheckin,estimatedCheckout);

        // Verificamos que al menos hay un dia entre las fechas.
        if (nroNights>=1) {
            // Tipos obligatorios
            this.userBooking = userBooking;
            this.statusbooking = StatusBooking.PENDING;
            this.userHouse = userHouse;
            // Tiempos estimados introducidos
            this.estimatedCheckin = estimatedCheckin;
            this.estimatedCheckout = estimatedCheckout;
            // Nro de noches
            this.numberNights = nroNights;
            this.totalPrice = nroNights * this.userHouse.getPricePerNight();
        }
    }

    // Se confirma la peticion de alquiler
    public void confirmedBooking() {
        if
        (this.statusbooking == StatusBooking.PENDING) {
            this.statusbooking=StatusBooking.CONFIRMED;
        }
    }

    public void cancelledBooking() {
        if
        ((this.statusbooking == StatusBooking.PENDING) ||
        (this.statusbooking == StatusBooking.CONFIRMED))
        {
            this.statusbooking=StatusBooking.CANCELLED;
        }
    }

    // Se hace el checkinout (real)
    public void completedBookingIn(LocalDateTime completedmoment) {
        if
        ((this.statusbooking == StatusBooking.CONFIRMED) ||
           (this.statusbooking == StatusBooking.RECOMMENDED))
        {
            this.checkin = completedmoment;
        }
    }

    // Se hace el checkout (real)
    public void completedBookingOut(LocalDateTime completedmoment) {
        if
        (((this.statusbooking == StatusBooking.CONFIRMED) ||
                (this.statusbooking == StatusBooking.RECOMMENDED))
            && (this.checkin != null))
        {
            this.statusbooking=StatusBooking.COMPLETED;
            this.checkout = completedmoment;
        }
    }


    // La reserva ha sido recomendada
    public void putAsRecommeded() {
        if
        ((this.statusbooking == StatusBooking.CONFIRMED) ||
           (this.statusbooking == StatusBooking.COMPLETED)) {
            this.statusbooking=StatusBooking.RECOMMENDED;
        }
    }

}
