package com.demo.config;

import com.demo.model.House;
import com.demo.model.StatusReserva;
import com.demo.model.User;
import com.demo.repository.HouseRepository;
import com.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Profile("!test")
public class HouseDataInitializer implements CommandLineRunner {

    private final HouseRepository houseRepository;
    private final UserRepository userRepository;

    public HouseDataInitializer(HouseRepository houseRepository, UserRepository userRepository) {
        this.houseRepository = houseRepository;
        this.userRepository = userRepository;
    }


    @Override
    public void run(String... args) {

        if (houseRepository.count() > 0) return;

        // Crear usuarios (hosts)
        User host1 = new User();
        host1.setUsername("juan");
        host1.setEmail("juan@test.com");
        userRepository.save(host1);

        User host2 = new User();
        host2.setUsername("maria");
        host2.setEmail("nala@test.com");
        userRepository.save(host2);

        // Crear casas con host asignados
        houseRepository.save(House.builder()
                        .title("Casa 1")
                        .description("Casa 1 descripción")
                        .pricePerNight(100d)
                        .location("Calle Principe Vergara 108")
                        .province("Madrid")
                        .maxGuests(3)
                        .host(host1)
        .build());

        houseRepository.save(new House(
                null,
                "casa 2",
                "casa 2 descr",
                120d,
                "Calle alcalá",
                "Madrid",
                4,
                LocalDateTime.now(),
                StatusReserva.DISPONIBLE,
                null,
                null,
                host1
        ));


    }

}

