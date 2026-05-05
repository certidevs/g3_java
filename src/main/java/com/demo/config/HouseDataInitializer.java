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

        //if (houseRepository.count() > 0) return;

        // Crear usuarios (hosts)
        User host1 = new User();
        host1.setUsername("juan");
        host1.setEmail("juan@test.com");
        userRepository.save(host1);

        User host2 = new User();
        host2.setUsername("maria");
        host2.setEmail("nala@test.com");
        userRepository.save(host2);

        User host3 = new User();
        host3.setUsername("PRUEBA");
        host3.setEmail("PRUEBA@test.com");
        userRepository.save(host3);

        // Crear casa sin host asignado
        House h1 = houseRepository.save(House.builder()
                .title("prueba 100")
                .description("Casa 1 descripción")
                .pricePerNight(100d)
                .location("Calle Principe Vergara 108")
                .province("Valencia")
                .maxGuests(3)
                .reserve(StatusReserva.RESERVADA)
                .build());

        // Crear casas con host asignados
        House house1 = houseRepository.save(House.builder()
                        .title("tu Casa")
                        .description("Casa 1 descripción")
                        .pricePerNight(100d)
                        .location("Calle Principe Vergara 108")
                        .province("Madrid")
                        .maxGuests(3)
                        .reserve(StatusReserva.NO_DISPONIBLE)
                        .host(host1)
        .build());

        House house2 = houseRepository.save(House.builder()
                .title("tu Casita")
                .description("Casa 2 descripción")
                .pricePerNight(100d)
                .location("Por ahi")
                .province("Barcelona")
                .maxGuests(6)
                .reserve(StatusReserva.RESERVADA)
                .host(host2)
                .build());


    }

}

