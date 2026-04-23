package com.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private Long userBooking;

    // Tiempo de chequeo estimado
    private LocalDateTime estimatedCheckin;
    private LocalDateTime estimatedCheckout;

    private Integer numberNights;

    // Tiempos de chequeos confirmados
    private LocalDateTime checkin;
    private LocalDateTime checkout;

    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private StatusBooking statusbooking = StatusBooking.PENDING;

    // ManyToOne a House
     // A MEJORAR
    private Long userHouse;

    public Booking(Long userBooking,Long userHouse)
    {
        this.userBooking=userBooking;
        this.statusbooking=StatusBooking.PENDING;
        this.userHouse=userHouse;
    }

    // Se hace el checkin (real)
    public void confirmedBooking(LocalDateTime confirmationmoment ) {
        if
        (this.statusbooking == StatusBooking.PENDING) {
            this.statusbooking=StatusBooking.CONFIRMED;
            this.checkin = confirmationmoment;
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
    public void completedBooking(LocalDateTime completedmoment) {
        if
        (this.statusbooking == StatusBooking.CONFIRMED) {
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
