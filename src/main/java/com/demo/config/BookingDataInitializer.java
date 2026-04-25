package com.demo.config;

import com.demo.model.Booking;
import com.demo.model.House;
import com.demo.model.User;
import com.demo.repository.BookingRepository;
import com.demo.repository.HouseRepository;
import com.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
@Profile("!test")
public class BookingDataInitializer implements CommandLineRunner {

    private final HouseRepository houseRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    public BookingDataInitializer(HouseRepository houseRepository, UserRepository userRepository,
                                  BookingRepository bookingRepository) {
        this.houseRepository = houseRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public void run(String... args) {

        // Crear usuarios (guest)
        User guest1_booking = new User();
        guest1_booking.setUsername("luis");
        guest1_booking.setEmail("luis@test.com");
        userRepository.save(guest1_booking);

        User guest2_booking = new User();
        guest2_booking.setUsername("alba");
        guest2_booking.setEmail("alba@test.com");
        userRepository.save(guest2_booking);


        // Crear casas con host asignados
        House house1_booking = new House(
                "Parcela el Viso",
                "con piscina",
                145.0,
                "Toledo",
                5,
                guest2_booking
        );
        houseRepository.save(house1_booking);

        House house2_booking = new House(
                "Apartamento",
                "Playa cercana",
                100.0,
                "Alicante",
                3,
                guest1_booking
        );
        houseRepository.save(house2_booking);


        // Reserva pendiente
        LocalDateTime timeIn =  LocalDateTime.of(2026,4,12,12,0);
        LocalDateTime timeOut =  LocalDateTime.of(2026,4,15,12,0);
        Booking reserva1 = new Booking(guest1_booking,house1_booking,timeIn,timeOut);
        bookingRepository.save(reserva1);

        LocalDateTime timeIn1 =  LocalDateTime.of(2026,4,12,12,0);
        LocalDateTime timeOut1 =  LocalDateTime.of(2026,4,15,12,0);
        Booking reserva2 = new Booking(guest2_booking,house2_booking,timeIn1,timeOut1);
        bookingRepository.save(reserva2);

    }

}


