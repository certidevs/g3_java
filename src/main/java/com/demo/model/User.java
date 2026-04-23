package com.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre de usuario único, utilizado para el login. */
    @Column(nullable = false, unique = true)
    private String username;
    private String firstName;
    private String lastName;

    /** Email único del usuario. */
    @Column(nullable = false, unique = true)
    private String email;

//    /** Contraseña codificada con {@link org.springframework.security.crypto.password.DelegatingPasswordEncoder}. */
//    @Column(nullable = false)
    private String password;

//    /** Rol del usuario que determina sus permisos en el sistema. */
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private Role role;
}
