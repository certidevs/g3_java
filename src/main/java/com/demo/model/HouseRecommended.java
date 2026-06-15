package com.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Entity
@Table(name = "houses_recommended")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HouseRecommended {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
    private String tokenFrom;
    private String tokenTo;

    private String emailFrom;
    private String emailTo;

    private String firstNameFrom;
    private String lastNameFrom;
    private String firstNameTo;
    private String lastNameTo;
    */

    @Column(length = 1000)
    private String message;

    @Builder.Default
    private LocalDateTime timeRecommended = LocalDateTime.now();

    @ManyToOne
    @ToString.Exclude
    private House houseRecommended;

    @ManyToOne
    @ToString.Exclude
    private User userFrom;

    @ManyToOne
    @ToString.Exclude
    private User userTo;

    @Builder.Default
    @Column(nullable = false)
    private Boolean viewed = false;
}
