package com.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "review")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Review {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: Una ventaja de Review, es que ninguna clase depende de ella, así que se puede considerar eliminar directamente la review
    // TODO: En lugar de active, quizás se puede usar algo como verified, que habla sobre si el usuario que realizo la review también realizo una reserva
    @Builder.Default
    @Column(columnDefinition = "boolean default true")
    private Boolean active = true;

    private Integer rating;

    @Column(length = 1000)
    private String comment;

    @CreationTimestamp
    // @Builder.Default // Checkear si es necesario, pues al hacerlo desde bdd da igual que el builder lo settee a NULL
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt; // = LocalDateTime.now();

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "house_id")
    private House house;

}