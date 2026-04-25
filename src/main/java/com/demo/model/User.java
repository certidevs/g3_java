package com.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.security.SecureRandom;

@Entity
@Getter
@Setter
@Table(name = "users")
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

    // Token para compartir entre usuarios bookings.
    private String tokenforRecommended;

    public User() {
        // Creamos un token propio del usuario.
        SecureRandom scr = new SecureRandom();
        StringBuilder sb = new StringBuilder(8);
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < 8; i++) {
            int index = scr.nextInt(caracteres.length());
            sb.append(caracteres.charAt(index));
        }
        this.tokenforRecommended = sb.toString();
    }
}
