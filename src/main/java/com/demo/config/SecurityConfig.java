package com.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    //paswordEncoder PARA CIFRAR CONTRAEÑAS

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //SecurityFilterChain PROTEGER ACCESOS A RUTAS

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));

        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin())); // h2 usa iframes

        http.authorizeHttpRequests(
                auth -> auth
                        // rutas publicas tanto GET como POST
                        .requestMatchers( "/", "/login",
                                "/register", "/css/**", "/images/**", "/js/**", "/webjars/**").permitAll()

                        // de golpe:
//                .requestMatchers(HttpMethod.GET, "/restaurants", "/restaurants/*", "/dishes", "/dishes/*").permitAll()

                        // separado de una en una
                        .requestMatchers(HttpMethod.GET, "/houses").permitAll()
                        .requestMatchers(HttpMethod.GET, "/houses/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/houses").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/houses/deactivate/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/houses/new").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/houses/edit/*").hasRole("ADMIN")
//
                        .requestMatchers(HttpMethod.GET, "/panel-control/*").authenticated()

//                        .requestMatchers(HttpMethod.GET, "/reviews").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/reviews").authenticated()
//                        .requestMatchers(HttpMethod.GET, "/reviews/new").authenticated()
//                        .requestMatchers(HttpMethod.GET , "/reviews/edit/*").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.GET , "/reviews/delete/*").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/reviews/*").permitAll()

                        // solo user normal, no admin
//                .requestMatchers(HttpMethod.GET, "/orders").hasRole("USER")
//                .requestMatchers(HttpMethod.GET, "/orders/new").hasRole("USER")
//                .requestMatchers(HttpMethod.POST, "/orders/**").hasRole("USER")
                        // todos los roles
                        .requestMatchers("/orders", "/orders/**").authenticated()
                        .requestMatchers("/users/new").hasRole("ADMIN")

                        .requestMatchers("/profile").authenticated()

                        // lo demás autenticado si o si
                        .anyRequest().authenticated()
        );

        http.formLogin(form ->
                form.loginPage("/login")
                        .defaultSuccessUrl("/houses", true)
                        .permitAll()
        );

        // TODO h2

        // TODO logout

        return http.build();
    }



}
