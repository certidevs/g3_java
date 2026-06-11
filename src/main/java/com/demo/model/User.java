package com.demo.model;

import com.demo.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Builder
@ToString
@Entity
@Getter
@Setter
@Table(name = "users")
public class User implements UserDetails{
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

    /** Contraseña codificada con {@link org.springframework.security.crypto.password.DelegatingPasswordEncoder}. */
    @Column(nullable = false)
    private String password;

    /** Rol del usuario que determina sus permisos en el sistema. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder.Default
    @Column(columnDefinition = "boolean default true", nullable = false)
    private Boolean active = true;

    // Token para compartir entre usuarios bookings.
    private String tokenforRecommended;

    public User() { // TODO: QUE ES ESTO ??, debería moverse a la logica de creación desde la bdd o por el estilo, que pasa cuando un User no se crea desde aca?????
        // Creamos un token propio del usuario.
        active = true;
        SecureRandom scr = new SecureRandom();
        StringBuilder sb = new StringBuilder(8);
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < 8; i++) {
            int index = scr.nextInt(caracteres.length());
            sb.append(caracteres.charAt(index));
        }
        this.tokenforRecommended = sb.toString();
    }

    @Override
    @NullMarked
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
//    @Override
//    public boolean isAccountNonExpired() {
//        return UserDetails.super.isAccountNonExpired();
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return UserDetails.super.isAccountNonLocked();
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return UserDetails.super.isCredentialsNonExpired();
//    }
//
}
