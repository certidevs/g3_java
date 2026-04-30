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
public class HouseRecommended {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tokenFrom;
    private String tokenTo;

    private String message;

    @Builder.Default
    private LocalDateTime timeRecommended = LocalDateTime.now();

    @ToString.Exclude
    @ManyToOne
    private House houseRecommended;

}
