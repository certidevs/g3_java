package com.demo.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
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
    private User user;

//Constructor
    public House() {
    }

}
