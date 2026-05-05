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




//        houseRepository.save(new House(
//                "La casita",
//                "casa 2 descr",
//                120d,
//                "Calle alcalá",
//                "Madrid",
//                4,
//                LocalDateTime.now(),
//                StatusReserva.NO_DISPONIBLE,
//                null,
//                null,
//                host2
//        ));


    }

}

