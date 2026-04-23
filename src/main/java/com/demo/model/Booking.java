package com.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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

    private LocalDate checkin;

    private LocalDate checkout;

    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private StatusBooking statusbooking;

    // ManyToOne a House
     // A MEJORAR
    private Long userHouse;

    public Booking(Long userBooking,Long userHouse)
    {
        this.userBooking=userBooking;
        this.statusbooking=StatusBooking.PENDING;
        this.userHouse=userHouse;
    }

}
