package com.demo.config;

import com.demo.model.House;
import com.demo.repository.HouseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class HouseDataInitializer implements CommandLineRunner {

    private final HouseRepository houseRepository;

    public HouseDataInitializer(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }
    @Override
    public void run(String... args) {

        if (houseRepository.count() > 0) return;

        houseRepository.save(new House(
                "Ático en Gran Vía",
                "Vistas al centro",
                120.0,
                "Madrid",
                4
        ));

        houseRepository.save(new House(
                "Casa rural en la sierra",
                "Jardín y tranquilidad",
                85.0,
                "Segovia",
                6
        ));

        houseRepository.save(new House(
                "Estudio en la playa",
                "A 50m del mar",
                70.0,
                "Valencia",
                2
        ));
    }

}

