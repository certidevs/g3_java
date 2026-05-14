package com.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "amenities")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Amenity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;            // identificador único

    @Column(nullable = false, length = 100)
    private String name;        // nombre del equipamiento ("WiFi")

    @Column(length = 500)
    private String description; // descripción detallada

    @Column(length = 100)
    private String icon;        // nombre del icono ("wifi", "pool", "parking")

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "amenities")
    private Set<House> houses = new HashSet<>();

}

