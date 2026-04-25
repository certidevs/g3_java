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
        guest1_booking.setTokenforRecommended("12345678");
        userRepository.save(guest1_booking);

        User guest2_booking = new User();
        guest2_booking.setUsername("alba");
        guest2_booking.setEmail("alba@test.com");
        guest1_booking.setTokenforRecommended("34561234");
        userRepository.save(guest2_booking);

        User guest3_booking = new User();
        guest3_booking.setUsername("jose");
        guest3_booking.setEmail("jose@test.com");
        guest1_booking.setTokenforRecommended("abdnbgrt");
        userRepository.save(guest3_booking);

        User guest4_booking = new User();
        guest4_booking.setUsername("carlos");
        guest4_booking.setEmail("carlos@test.com");
        guest1_booking.setTokenforRecommended("556kkiio");
        userRepository.save(guest4_booking);




        // Crear casas con host asignados
        House house1_booking = new House(
                "Parcela el Viso",
                "con piscina",
                145.0,
                "Toledo",
                5,
                guest1_booking
        );
        houseRepository.save(house1_booking);

        House house2_booking = new House(
                "Apartamento",
                "Playa cercana",
                100.0,
                "Alicante",
                3,
                guest2_booking
        );
        houseRepository.save(house2_booking);


        House house3_booking = new House(
                "Rural Torres",
                "senderismo sierra",
                48.0,
                "Jaén",
                4,
                guest3_booking
        );
        houseRepository.save(house3_booking);

        House house4_booking = new House(
                "Camping",
                "en playa con piscina",
                100.0,
                "Barcelona",
                4,
                guest4_booking
        );
        houseRepository.save(house4_booking);



        // Reserva pendiente
        LocalDateTime timeIn =  LocalDateTime.of(2026,4,12,12,0);
        LocalDateTime timeOut =  LocalDateTime.of(2026,4,15,12,0);
        Booking reserva1 = new Booking(guest1_booking,house1_booking,timeIn,timeOut);
        bookingRepository.save(reserva1);

        LocalDateTime timeIn1 =  LocalDateTime.of(2026,6,5,12,0);
        LocalDateTime timeOut1 =  LocalDateTime.of(2026,6,15,12,0);
        Booking reserva2 = new Booking(guest2_booking,house2_booking,timeIn1,timeOut1);
        bookingRepository.save(reserva2);

        // Reserva de tipo confirmada
        LocalDateTime timeIn2 =  LocalDateTime.of(2026,8,5,12,0);
        LocalDateTime timeOut2 =  LocalDateTime.of(2026,8,10,12,0);
        Booking reserva3 = new Booking(guest3_booking,house3_booking,timeIn2,timeOut2);
        // Modificamos el alquiler como confirmado
        reserva3.confirmedBooking();
        // y guardamos
        bookingRepository.save(reserva3);

        // Reserva de tipo confirmada
        LocalDateTime timeIn3 =  LocalDateTime.of(2026,7,12,12,0);
        LocalDateTime timeOut3 =  LocalDateTime.of(2026,7,15,12,0);
        Booking reserva4 = new Booking(guest4_booking,house4_booking,timeIn3,timeOut3);
        // Modificamos el alquiler como confirmado
        reserva4.confirmedBooking();
        // y guardamos
        bookingRepository.save(reserva4);



    }

}


