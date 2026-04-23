package com.demo.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "houses")
public class House {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private  String description;
    private Double pricePerNight;
    private String location;
    private Integer maxGuests;

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
