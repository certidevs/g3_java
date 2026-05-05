package com.demo.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Entity
@Table(name = "houses")
@AllArgsConstructor
@NoArgsConstructor
public class House {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Column(length = 1000)
    private  String description;
    private Double pricePerNight;
    private String location;
    private String province;
    private Integer maxGuests;

    @Builder.Default
    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean active = true;

    @Builder.Default
    private LocalDateTime timeRecommended = LocalDateTime.now();

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private StatusReserva reserve = StatusReserva.DISPONIBLE;

    @ToString.Exclude
    @ManyToOne
    private User tokenFrom;

    @ToString.Exclude
    @ManyToOne
    private User tokenTo;

    @ManyToOne
    @JoinColumn(name = "host_id")
    private User host;

//Constructor
//    public House() {
//    }

//    public House( String title, String description, Double pricePerNight, String location, Integer maxGuests, User host) {
//
//        this.title = title;
//        this.description = description;
//        this.pricePerNight = pricePerNight;
//        this.location = location;
//        this.maxGuests = maxGuests;
//        this.host = host;
//    }
}
