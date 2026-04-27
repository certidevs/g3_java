package com.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "amenities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}

