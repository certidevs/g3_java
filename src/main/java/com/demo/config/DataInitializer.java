package com.demo.config;

import com.demo.model.*;
import com.demo.repository.*;
import com.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

//
//@Component
//@Profile("!test")
@Component
@Profile("!test")
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final HouseRepository houseRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;
    private final AmenityRepository amenityRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

//    public DataInitializer(HouseRepository houseRepository, UserRepository userRepository,
//                           BookingRepository bookingRepository, ReviewRepository reviewRepository,
//                           AmenityRepository amenityRepository,
//                           PasswordEncoder passwordEncoder, UserService userService) {
//        this.houseRepository = houseRepository;
//        this.userRepository = userRepository;
//        this.bookingRepository = bookingRepository;
//        this.reviewRepository = reviewRepository;
//        this.amenityRepository = amenityRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.userService = userService;
//    }

    @Override
    public void run(String... args) {

//        if (amenityRepository.count() == 0) {
//
//            amenityRepository.save(
//                    Amenity.builder()
//                            .name("Wifi")
//                            .description("Internet de alta velocidad")
//                            .icon("wifi")
//                            .build()
//            );
//
//            amenityRepository.save(
//                    Amenity.builder()
//                            .name("Piscina")
//                            .description("Piscina privada")
//                            .icon("water-ladder")
//                            .build()
//            );
//
//            amenityRepository.save(
//                    Amenity.builder()
//                            .name("Parking")
//                            .description("Estacionamiento gratuito")
//                            .icon("square-parking")
//                            .build()
//            );
//
//            amenityRepository.save(
//                    Amenity.builder()
//                            .name("Cocina")
//                            .description("Cocina equipada")
//                            .icon("utensils")
//                            .build()
//            );
        Amenity wifi = amenityRepository.save(
                Amenity.builder()
                        .name("Wifi")
                        .description("Internet de alta velocidad")
                        .icon("wifi")
                        .build()
        );

        Amenity piscina = amenityRepository.save(
                Amenity.builder()
                        .name("Piscina")
                        .description("Piscina privada")
                        .icon("water-ladder")
                        .build()
        );

        Amenity parking = amenityRepository.save(
                Amenity.builder()
                        .name("Parking")
                        .description("Parking gratuito")
                        .icon("square-parking")
                        .build()
        );

        Amenity tenis = amenityRepository.save(
                Amenity.builder()
                        .name("Pista de tenis")
                        .description("Cancha privada")
                        .icon("table-tennis-paddle-ball")
                        .build()
        );
        Amenity mascotas = amenityRepository.save(
                Amenity.builder()
                        .name("Admite mascotas")
                        .description("Trae y comparte con tu mascota")
                        .icon("paw")
                        .build()
        );

        Amenity cocina = amenityRepository.save(
                    Amenity.builder()
                         .name("Cocina")
                         .description("Cocina equipada")
                         .icon("utensils")
                         .build()
            );
        //}


        //////////////// Datos verificados en las relaciones

        // Guests
        User guest_test_booking = new User();
        guest_test_booking.setUsername("Guest prueba");
        guest_test_booking.setFirstName("Guest 1");
        guest_test_booking.setLastName("Guest 1");
        guest_test_booking.setEmail("guest1@test1.com");
        guest_test_booking.setPassword("password");
        guest_test_booking.setRole(Role.ROLE_USER);
        userRepository.save(guest_test_booking);

        User guest_test_booking_1 = new User();
        guest_test_booking_1.setUsername("Guest prueba 2");
        guest_test_booking_1.setFirstName("Guest 2");
        guest_test_booking_1.setLastName("Guest 2");
        guest_test_booking_1.setEmail("guest2@test1.com");
        guest_test_booking_1.setPassword("password");
        guest_test_booking_1.setRole(Role.ROLE_USER);
        userRepository.save(guest_test_booking_1);

        // Host
        User host_test_booking = new User();
        host_test_booking.setUsername("Host prueba");
        host_test_booking.setFirstName("Host 1");
        host_test_booking.setLastName("Host 1");
        host_test_booking.setEmail("host1@test1.com");
        host_test_booking.setPassword("password");
        host_test_booking.setRole(Role.ROLE_ADMIN);
        userRepository.save(host_test_booking);

        // Casa propiedad de Host1
        House house_test_booking = houseRepository.save(House.builder()
                .title("Parcela el Viso")
                .description("con piscina")
                .pricePerNight(145.0)
                .location("Fuenlabrada")
                .province("Madrid")
                .maxGuests(5)
                .host(host_test_booking)
                .houseType(HouseType.CASA)
                .imageUrl("h1.jpg")
                .amenities(Set.of(wifi, piscina, parking,mascotas,cocina))
                .build()
        );

        // Casa propiedad de Host1
        House house_test_booking1 = houseRepository.save(House.builder()
                .title("Parcela Carranque")
                .description("con pista de tenis")
                .pricePerNight(220.0)
                .location("Carranque")
                .province("Toledo")
                .maxGuests(8)
                .imageUrl("h2.jpg")
                .houseType(HouseType.APARTAMENTO)
                .host(host_test_booking)
                .amenities(Set.of(wifi, tenis))
                .build()
        );

        // Reserva pendiente
        LocalDateTime timeIn_1 =  LocalDateTime.of(2026,4,12,12,0);
        LocalDateTime timeOut_1 =  LocalDateTime.of(2026,4,15,12,0);
        Booking reserva_test = new Booking(guest_test_booking,house_test_booking,timeIn_1,timeOut_1);
        bookingRepository.save(reserva_test);

        LocalDateTime timeIn_2 =  LocalDateTime.of(2026,4,7,12,0);
        LocalDateTime timeOut_2 =  LocalDateTime.of(2026,4,15,12,0);
        Booking reserva_test1 = new Booking(guest_test_booking_1,house_test_booking1,timeIn_2,timeOut_2);
        bookingRepository.save(reserva_test1);

        //////////////////////

        User user1 = new User();
        user1.setFirstName("user1");
        user1.setLastName("user1");
        user1.setUsername("user1");
        user1.setEmail("user1@gmail.com");
        user1.setPassword(passwordEncoder.encode("user1"));
        user1.setRole(Role.ROLE_USER);
        userRepository.save(user1);

        User user2 = new User();
        user2.setFirstName("user2");
        user2.setLastName("user2");
        user2.setUsername("user2");
        user2.setEmail("user2@gmail.com");
        user2.setPassword(passwordEncoder.encode("user2"));
        user2.setRole(Role.ROLE_USER);
        userRepository.save(user2);

        User host1 = new User();
        host1.setUsername("host1");
        host1.setEmail("host1@gmail.com");
        host1.setPassword(passwordEncoder.encode("host1"));
        host1.setRole(Role.ROLE_ADMIN);
        userRepository.save(host1);

        // Casa propiedad de Host1
        House house_test_booking11 = houseRepository.save(House.builder()
                .title("Apartamento")
                .description("céntrico")
                .pricePerNight(105.0)
                .location("Barcelona")
                .province("Barcelona")
                .maxGuests(5)
                .host(host_test_booking)
                .houseType(HouseType.CASA)
                .imageUrl("h1.jpg")
                .amenities(Set.of(wifi, piscina, parking,mascotas,cocina))
                .host(host1)
                .build()
        );

        // Casa propiedad de Host1
        House house_test_booking12 = houseRepository.save(House.builder()
                .title("Hostal el Limón")
                .description("Barrio El Casar")
                .pricePerNight(25.0)
                .location("Getafe")
                .province("Madrid")
                .maxGuests(3)
                .host(host1)
                .houseType(HouseType.CASA)
                .imageUrl("h1.jpg")
                .amenities(Set.of(wifi, piscina, parking,mascotas,cocina))
                .build()
        );




        // user host
        // casa

        // user guest
        // booking

        // Crear usuarios (hosts)
        User host11 = new User();
        host11.setUsername("juan");
        host11.setEmail("juan@test.com");
        host11.setPassword("password");
        host11.setRole(Role.ROLE_ADMIN);
        userRepository.save(host11);

        User host2 = new User();
        host2.setUsername("maria");
        host2.setEmail("nala@test.com");
        host2.setPassword("password");
        host2.setRole(Role.ROLE_ADMIN);
        userRepository.save(host2);

        User host3 = new User();
        host3.setUsername("PRUEBA");
        host3.setEmail("PRUEBA@test.com");
        host3.setPassword("password");
        host3.setRole(Role.ROLE_ADMIN);
        userRepository.save(host3);

        // Crear usuarios (guest)
        User guest1_booking = new User();
        guest1_booking.setUsername("luis1");
        guest1_booking.setFirstName("Luis");
        guest1_booking.setLastName("López");
        guest1_booking.setEmail("luis@test1.com");
        guest1_booking.setPassword("password");
        guest1_booking.setRole(Role.ROLE_USER);
        userRepository.save(guest1_booking);

        User guest2_booking = new User();
        guest2_booking.setUsername("alba2");
        guest2_booking.setFirstName("Alba");
        guest2_booking.setLastName("Martínez");
        guest2_booking.setEmail("alba@test2.com");
        guest2_booking.setPassword("password");
        guest2_booking.setRole(Role.ROLE_USER);
        userRepository.save(guest2_booking);

        User guest3_booking = new User();
        guest3_booking.setUsername("jose3");
        guest3_booking.setEmail("jose@test3.com");
        guest3_booking.setPassword("password");
        guest3_booking.setRole(Role.ROLE_USER);
        userRepository.save(guest3_booking);

        User guest4_booking = new User();
        guest4_booking.setUsername("carlos4");
        guest4_booking.setEmail("carlos@4test.com");
        guest4_booking.setPassword("password");
        guest4_booking.setRole(Role.ROLE_USER);
        userRepository.save(guest4_booking);

        User guest5_booking = new User();
        guest5_booking.setUsername("pedro5");
        guest5_booking.setEmail("pedro@5test.com");
        guest5_booking.setPassword("password");
        guest5_booking.setRole(Role.ROLE_USER);
        userRepository.save(guest5_booking);

        // Crear usuarios (guest)
        User host1_booking = new User();
        host1_booking.setUsername("luis6");
        host1_booking.setEmail("luis@test6.com");
        host1_booking.setPassword("password");
        host1_booking.setRole(Role.ROLE_USER);
        userRepository.save(host1_booking);

        User host2_booking = new User();
        host2_booking.setUsername("alba7");
        host2_booking.setEmail("alba@test7.com");
        host2_booking.setPassword("password");
        host2_booking.setRole(Role.ROLE_USER);
        host2_booking.setTokenforRecommended(userService.generateRecommendedToken());
        userRepository.save(host2_booking);

        User host3_booking = new User();
        host3_booking.setUsername("jose8");
        host3_booking.setEmail("jose@test.8com");
        host3_booking.setPassword("password");
        host3_booking.setRole(Role.ROLE_USER);
        userRepository.save(host3_booking);

        User host4_booking = new User();
        host4_booking.setUsername("carlos9");
        host4_booking.setEmail("carlos@test9.com");
        host4_booking.setPassword("password");
        host4_booking.setRole(Role.ROLE_USER);
        userRepository.save(host4_booking);

        User host5_booking = new User();
        host5_booking.setUsername("pedro10");
        host5_booking.setEmail("pedro@test10.com");
        host5_booking.setPassword("password");
        host5_booking.setRole(Role.ROLE_USER);
        userRepository.save(host5_booking);

        // Crear casas con host asignados
        House house1 = houseRepository.save(House.builder()
                .title("tu Casa")
                .description("Casa 1 descripción")
                .pricePerNight(100d)
                .location("Calle Principe Vergara 108")
                .province("Madrid")
                .maxGuests(3)
                .imageUrl("h4.jpg")
                .amenities(Set.of(wifi,cocina))
                .houseType(HouseType.CASA)
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
                .imageUrl("h5.jpg")
                .amenities(Set.of(wifi,mascotas,cocina))
                .houseType(HouseType.CASA)
                .reserve(StatusReserva.RESERVADA)
                .host(host2)
                .build());


        House house1_booking = houseRepository.save(House.builder()
                        .title("Parcela el Viso")
                        .description("con piscina")
                        .pricePerNight(145.0)
                        .location("Toledo")
                        .province("Madrid")
                        .imageUrl("h6.jpg")
                        .maxGuests(5)
                        .amenities(Set.of(wifi, piscina, parking,mascotas,cocina))
                        .houseType(HouseType.CASA)
                        .host(host1_booking)
                        .build()
        );


        House house2_booking = houseRepository.save(House.builder()
                .title("Apartamento")
                .description("Playa cercana")
                .pricePerNight(145.0)
                .location("alicante")
                .province("Madrid")
                .maxGuests(3)
                .imageUrl("h7.jpg")
                .houseType(HouseType.CASA)
                .amenities(Set.of(wifi, parking,mascotas,cocina))
                .host(host2_booking)
                .build()
        );

//
        House house3_booking = houseRepository.save(House.builder()
                .title("Rural Torres")
                .description("senderismo sierra")
                .pricePerNight(45.0)
                .maxGuests(4)
                .imageUrl("h8.jpg")
                .houseType(HouseType.CASA)
                .amenities(Set.of(wifi, parking,mascotas,cocina))
                .host(host3_booking)
                .build()
        );


        House house4_booking = houseRepository.save(House.builder()
                .title("Camping")
                .description("en playa con piscina")
                .pricePerNight(105.0)
                .maxGuests(4)
                .imageUrl("h1.jpg")
                .houseType(HouseType.CASA)
                .amenities(Set.of( piscina, parking,mascotas,cocina))
                .host(host4_booking)
                .build()
        );
        houseRepository.save(house4_booking);

        House house5_booking = houseRepository.save(House.builder()
                .title("Ático")
                .description("centro ciudad")
                .pricePerNight(250.6)
                .province("Gijón")
                .maxGuests(2)
                .imageUrl("h2.jpg")
                .houseType(HouseType.CASA)
                .amenities(Set.of(wifi, parking,mascotas,cocina))
                .host(host5_booking)
                .build()
        );
        houseRepository.save(house5_booking);

        House house6_booking = houseRepository.save(House.builder()
                .title("Ático1")
                .description("centro ciudad1")
                .pricePerNight(167.6)
                .province("Gijón1")
                .maxGuests(6)
                .imageUrl("h3.jpg")
                .houseType(HouseType.CASA)
                .amenities(Set.of(wifi,parking,mascotas,cocina))
                .host(host5_booking)
                .build()
        );
        houseRepository.save(house6_booking);

        House house7_booking = houseRepository.save(House.builder()
                .title("Ático2")
                .description("centro ciudad2")
                .pricePerNight(280.6)
                .province("Gijón2")
                .maxGuests(4)
                .imageUrl("h5.jpg")
                .amenities(Set.of(wifi, piscina, parking,mascotas,cocina))
                .houseType(HouseType.CASA)
                .host(host5_booking)
                .build()
        );
        houseRepository.save(house7_booking);

        // Crear casa sin host asignado
        House h1 = houseRepository.save(House.builder()
                .title("prueba 100")
                .description("Casa 1 descripción")
                .pricePerNight(100d)
                .location("Calle Principe Vergara 108")
                .province("Valencia")
                .imageUrl("h3.jpg")
                .maxGuests(3)
                .amenities(Set.of(wifi, piscina, parking,mascotas,cocina))
                .houseType(HouseType.HABITACION)
                .reserve(StatusReserva.RESERVADA)
                .build());

        // crear cuatro reviews de casa usando Builder de lombok

        Review review1 = Review.builder()
                .comment("Te atienden bien")
                .house(house_test_booking1)
                .title("IDEAL PARA PAREJAS")
                .rating(5)
                .build();

        Review review2 = Review.builder()
                .comment("Nefasto")
                .house(house_test_booking)
                .title("HERMOSO LUGAR PARA IR EN FAMILIA")
                .rating(1)
                .build();

        Review review3 = Review.builder()
                .comment("Ni fu ni fa")
                .house(house1_booking)
                .title("Comí y no me morí")
                .rating(3)
                .build();

        Review review4 = Review.builder()
                .comment("Ni fu ni fa")
                .house(house2_booking)
                .title("Me pusieron de menos")
                .rating(2)
                .build();

        Review review5 = Review.builder()
                .comment("Excelente")
                .house(house3_booking)
                .title("Guay")
                .rating(5)
                .build();

        reviewRepository.saveAll(List.of(review1, review2, review3, review4,  review5));

        // Reserva pendiente

        LocalDateTime timeIn =  LocalDateTime.of(2026,4,12,12,0);
        LocalDateTime timeOut =  LocalDateTime.of(2026,4,15,12,0);
        Booking reserva1 = new Booking(guest1_booking,house1_booking,timeIn,timeOut);
        bookingRepository.save(reserva1);

        LocalDateTime timeIn1 =  LocalDateTime.of(2026,6,5,12,0);
        LocalDateTime timeOut1 =  LocalDateTime.of(2026,6,15,12,0);
        Booking reserva2 = new Booking(guest2_booking,house2_booking,timeIn1,timeOut1);
        bookingRepository.save(reserva2);
//
        // Reserva de tipo confirmada
        LocalDateTime timeIn2 =  LocalDateTime.of(2026,8,5,12,0);
        LocalDateTime timeOut2 =  LocalDateTime.of(2026,8,10,12,0);
        Booking reserva3 = new Booking(guest3_booking,house3_booking,timeIn2,timeOut2);
//        // Modificamos el alquiler como confirmado
        reserva3.confirmedBooking();
//        // y guardamos
        bookingRepository.save(reserva3);

        //Reserva de tipo confirmada
        LocalDateTime timeIn3 =  LocalDateTime.of(2026,7,12,12,0);
        LocalDateTime timeOut3 =  LocalDateTime.of(2026,7,15,12,0);
        Booking reserva4 = new Booking(guest4_booking,house4_booking,timeIn3,timeOut3);
//        // Modificamos el alquiler como confirmado
        reserva4.confirmedBooking();
//        // y guardamos
        bookingRepository.save(reserva4);


        // Reserva cancelada
        LocalDateTime timeIn4 =  LocalDateTime.of(2026,9,1,12,0);
        LocalDateTime timeOut4 =  LocalDateTime.of(2026,12,1,12,0);
        Booking reserva5 = new Booking(guest5_booking,house4_booking,timeIn4,timeOut4);

//        // Modificamos el alquiler como confirmado
        reserva5.cancelledBooking();
//        // y guardamos
        bookingRepository.save(reserva5);
//
//        // Reserva pendiente
        LocalDateTime timeIn5 =  LocalDateTime.of(2026,4,12,12,0);
        LocalDateTime timeOut5 =  LocalDateTime.of(2026,4,15,12,0);
        Booking reserva6 = new Booking(guest1_booking,house2_booking,timeIn,timeOut);
        bookingRepository.save(reserva6);
//
//        // Reserva pendiente
        LocalDateTime timeIn6 =  LocalDateTime.of(2026,4,12,12,0);
        LocalDateTime timeOut6 =  LocalDateTime.of(2026,4,15,12,0);
        Booking reserva7 = new Booking(guest1_booking,house3_booking,timeIn,timeOut);
        bookingRepository.save(reserva7);

    }



}


