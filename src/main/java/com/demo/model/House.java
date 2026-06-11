package com.demo.model;
import com.demo.model.enums.HouseType;
import com.demo.model.enums.StatusReserva;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
    // TODO opcional minimumNights
    // TODO opcional maxNights

//    @Builder.Default
    @Enumerated(EnumType.STRING)
    private HouseType houseType;

    @Column(name = "image_url")
    private String imageUrl;

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

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "house_amenities",
            joinColumns = @JoinColumn(name = "house_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    private Set<Amenity> amenities = new HashSet<>();



}
