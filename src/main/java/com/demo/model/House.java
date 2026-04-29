package com.demo.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@Entity
@Table(name = "houses")
@AllArgsConstructor
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
    @Enumerated(EnumType.STRING)
    private StatusReserva reserve = StatusReserva.DISPONIBLE;

    @ManyToOne
    private User host;

//Constructor
    public House() {
    }

    public House( String title, String description, Double pricePerNight, String location, Integer maxGuests, User host) {

        this.title = title;
        this.description = description;
        this.pricePerNight = pricePerNight;
        this.location = location;
        this.maxGuests = maxGuests;
        this.host = host;
    }
}
