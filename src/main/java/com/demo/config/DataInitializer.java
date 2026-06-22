package com.demo.config;

import com.demo.model.*;
import com.demo.model.enums.HouseType;
import com.demo.model.enums.Province;
import com.demo.model.enums.Role;
import com.demo.model.enums.StatusReserva;
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

    private static final String PLACEHOLDER_IMAGE = "placeholder.jpg";

    private final HouseRepository houseRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;
    private final AmenityRepository amenityRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final FavoriteRepository favoriteRepository;
    private final HouseRecommendedRepository houseRecommendedRepository;

    @Override
    public void run(String... args) {

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
        guest_test_booking.setUsername("guest_prueba");
        guest_test_booking.setFirstName("Guest 1");
        guest_test_booking.setLastName("Guest 1");
        guest_test_booking.setEmail("guest1@test1.com");
        guest_test_booking.setPassword(passwordEncoder.encode("guest1"));
        guest_test_booking.setRole(Role.ROLE_USER);
        userRepository.save(guest_test_booking);

        User guest_test_booking_1 = new User();
        guest_test_booking_1.setUsername("guest_prueba_2");
        guest_test_booking_1.setFirstName("Guest 2");
        guest_test_booking_1.setLastName("Guest 2");
        guest_test_booking_1.setEmail("guest2@test1.com");
        guest_test_booking_1.setPassword(passwordEncoder.encode("guest2"));
        guest_test_booking_1.setRole(Role.ROLE_USER);
        userRepository.save(guest_test_booking_1);

        // Host
        User host_test_booking = new User();
        host_test_booking.setUsername("host_prueba");
        host_test_booking.setFirstName("Host 1");
        host_test_booking.setLastName("Host 1");
        host_test_booking.setEmail("host1@test1.com");
        host_test_booking.setPassword(passwordEncoder.encode("host1"));
        host_test_booking.setRole(Role.ROLE_ADMIN);
        userRepository.save(host_test_booking);

        // Casa propiedad de Host1
        House house_test_booking = houseRepository.save(House.builder()
                .title("Chalet el Viso con Piscina")
                .description("Amplio chalet con piscina privada y jardín, ideal para familias.")
                .pricePerNight(145.0)
                .location("Fuenlabrada")
                .province(Province.MADRID)
                .maxGuests(5)
                .host(host_test_booking)
                .houseType(HouseType.CASA)
                .imageUrl("h1.jpg")
                .amenities(Set.of(wifi, piscina, parking,mascotas,cocina))
                .build()
        );

        // Casa propiedad de Host1
        House house_test_booking1 = houseRepository.save(House.builder()
                .title("Villa Carranque con Pista de Tenis")
                .description("Magnífica villa con pista de tenis privada, barbacoa y amplias zonas verdes.")
                .pricePerNight(220.0)
                .location("Carranque")
                .province(Province.TOLEDO)
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
        user1.setFirstName("Miguel");
        user1.setLastName("Pérez");
        user1.setUsername("user1");
        user1.setEmail("user1@gmail.com");
        user1.setPassword(passwordEncoder.encode("user1"));
        user1.setRole(Role.ROLE_USER);
        userRepository.save(user1);

        User user2 = new User();
        user2.setFirstName("Castaño");
        user2.setLastName("Alejandro");
        user2.setUsername("user2");
        user2.setEmail("user2@gmail.com");
        user2.setPassword(passwordEncoder.encode("user2"));
        user2.setRole(Role.ROLE_USER);
        userRepository.save(user2);

        User host1 = new User();
        host1.setUsername("host1");
        host1.setFirstName("Joaquín");
        host1.setLastName("Ibáñez");
        host1.setEmail("host1@gmail.com");
        host1.setPassword(passwordEncoder.encode("host1"));
        host1.setRole(Role.ROLE_ADMIN);
        userRepository.save(host1);

        // Casa propiedad de Host1
        House house_test_booking11 = houseRepository.save(House.builder()
                .title("Apartamento Moderno en el Centro de Barcelona")
                .description("Elegante y luminoso apartamento situado en el corazón de Barcelona, a pocos pasos de los principales puntos de interés.")
                .pricePerNight(105.0)
                .location("Barcelona")
                .province(Province.BARCELONA)
                .maxGuests(5)
                .host(host_test_booking)
                .houseType(HouseType.APARTAMENTO)
                .imageUrl("h9.jpg")
                .amenities(Set.of(wifi, piscina, parking,mascotas,cocina))
                .host(host1)
                .build()
        );

        // Casa propiedad de Host1
        House house_test_booking12 = houseRepository.save(House.builder()
                .title("Habitación en Hostal el Limón")
                .description("Habitación acogedora y totalmente equipada en una zona tranquila de Getafe, excelente conexión de transporte.")
                .pricePerNight(25.0)
                .location("Getafe")
                .province(Province.MADRID)
                .maxGuests(3)
                .host(host1)
                .houseType(HouseType.HABITACION)
                .imageUrl("carrusel2.jpg")
                .amenities(Set.of(wifi, piscina, parking,mascotas,cocina))
                .build()
        );


        // Crear usuarios (hosts)
        User host11 = new User();
        host11.setUsername("juan");
        host11.setEmail("juan@test.com");
        host11.setPassword(passwordEncoder.encode("juan"));
        host11.setRole(Role.ROLE_ADMIN);
        userRepository.save(host11);

        User host2 = new User();
        host2.setUsername("maria");
        host2.setEmail("nala@test.com");
        host2.setPassword(passwordEncoder.encode("maria"));
        host2.setRole(Role.ROLE_ADMIN);
        userRepository.save(host2);

        User host3 = new User();
        host3.setUsername("PRUEBA");
        host3.setEmail("PRUEBA@test.com");
        host3.setPassword(passwordEncoder.encode("PRUEBA"));
        host3.setRole(Role.ROLE_ADMIN);
        userRepository.save(host3);

        // Crear usuarios (guest)
        User guest1_booking = new User();
        guest1_booking.setUsername("luis1");
        guest1_booking.setFirstName("Luis");
        guest1_booking.setLastName("López");
        guest1_booking.setEmail("luis@test1.com");
        guest1_booking.setPassword(passwordEncoder.encode("luis1"));
        guest1_booking.setRole(Role.ROLE_USER);
        userRepository.save(guest1_booking);

        User guest2_booking = new User();
        guest2_booking.setUsername("alba2");
        guest2_booking.setFirstName("Alba");
        guest2_booking.setLastName("Martínez");
        guest2_booking.setEmail("alba@test2.com");
        guest2_booking.setPassword(passwordEncoder.encode("alba2"));
        guest2_booking.setRole(Role.ROLE_USER);
        userRepository.save(guest2_booking);

        User guest3_booking = new User();
        guest3_booking.setUsername("jose3");
        guest3_booking.setEmail("jose@test3.com");
        guest3_booking.setPassword(passwordEncoder.encode("jose3"));
        guest3_booking.setRole(Role.ROLE_USER);
        userRepository.save(guest3_booking);

        User guest4_booking = new User();
        guest4_booking.setUsername("carlos4");
        guest4_booking.setEmail("carlos@4test.com");
        guest4_booking.setPassword(passwordEncoder.encode("carlos4"));
        guest4_booking.setRole(Role.ROLE_USER);
        userRepository.save(guest4_booking);

        User guest5_booking = new User();
        guest5_booking.setUsername("pedro5");
        guest5_booking.setEmail("pedro@5test.com");
        guest5_booking.setPassword(passwordEncoder.encode("pedro5"));
        guest5_booking.setRole(Role.ROLE_USER);
        userRepository.save(guest5_booking);

        // Crear usuarios (anfitriones)
        User host1_booking = new User();
        host1_booking.setUsername("luis6");
        host1_booking.setEmail("luis@test6.com");
        host1_booking.setPassword(passwordEncoder.encode("luis6"));
        host1_booking.setRole(Role.ROLE_ADMIN);
        userRepository.save(host1_booking);

        User host2_booking = new User();
        host2_booking.setUsername("alba7");
        host2_booking.setEmail("alba@test7.com");
        host2_booking.setPassword(passwordEncoder.encode("alba7"));
        host2_booking.setRole(Role.ROLE_ADMIN);
        host2_booking.setTokenforRecommended(userService.generateRecommendedToken());
        userRepository.save(host2_booking);

        User host3_booking = new User();
        host3_booking.setUsername("jose8");
        host3_booking.setEmail("jose@test.8com");
        host3_booking.setPassword(passwordEncoder.encode("jose8"));
        host3_booking.setRole(Role.ROLE_ADMIN);
        userRepository.save(host3_booking);

        User host4_booking = new User();
        host4_booking.setUsername("carlos9");
        host4_booking.setEmail("carlos@test9.com");
        host4_booking.setPassword(passwordEncoder.encode("carlos9"));
        host4_booking.setRole(Role.ROLE_ADMIN);
        userRepository.save(host4_booking);

        User host5_booking = new User();
        host5_booking.setUsername("pedro10");
        host5_booking.setEmail("pedro@test10.com");
        host5_booking.setPassword(passwordEncoder.encode("pedro10"));
        host5_booking.setRole(Role.ROLE_ADMIN);
        userRepository.save(host5_booking);

        // Crear casas con host asignados
        House house1 = houseRepository.save(House.builder()
                .title("Chalet de Diseño en Chamartín")
                .description("Exclusiva vivienda unifamiliar en una de las mejores zonas de Madrid, con excelentes calidades y jardín.")
                .pricePerNight(100d)
                .location("Calle Principe Vergara 108")
                .province(Province.MADRID)
                .maxGuests(3)
                .imageUrl("h4.jpg")
                .amenities(Set.of(wifi,cocina))
                .houseType(HouseType.CASA)
                .reserve(StatusReserva.NO_DISPONIBLE)
                .host(host1)
                .build());

        House house2 = houseRepository.save(House.builder()
                .title("Estudio Acogedor en Gracia")
                .description("Encantador estudio en el bohemio barrio de Gracia, perfecto para parejas y escapadas de fin de semana.")
                .pricePerNight(100d)
                .location("Barcelona")
                .province(Province.BARCELONA)
                .maxGuests(6)
                .imageUrl("h5.jpg")
                .amenities(Set.of(wifi,mascotas,cocina))
                .houseType(HouseType.CASA)
                .reserve(StatusReserva.RESERVADA)
                .host(host2)
                .build());


        House house1_booking = houseRepository.save(House.builder()
                        .title("Chalet Rural El Viso")
                        .description("Precioso chalet rural con piscina en el Viso de San Juan, ideal para desconectar de la ciudad.")
                        .pricePerNight(145.0)
                        .location("Viso de San Juan")
                        .province(Province.TOLEDO)
                        .imageUrl("h6.jpg")
                        .maxGuests(5)
                        .amenities(Set.of(wifi, piscina, parking,mascotas,cocina))
                        .houseType(HouseType.CASA)
                        .host(host1_booking)
                        .build()
        );


        House house2_booking = houseRepository.save(House.builder()
                .title("Apartamento Luminoso Cerca de la Playa")
                .description("Precioso apartamento reformado con vistas laterales al mar, ideal para disfrutar de tus vacaciones en Alicante.")
                .pricePerNight(145.0)
                .location("Alicante")
                .province(Province.ALICANTE)
                .maxGuests(3)
                .imageUrl("h7.jpg")
                .houseType(HouseType.CASA)
                .amenities(Set.of(wifi, parking,mascotas,cocina))
                .host(host2_booking)
                .build()
        );

//
        House house3_booking = houseRepository.save(House.builder()
                .title("Casa Rural Torres en la Sierra")
                .description("Casa rural con encanto, rodeada de naturaleza. Perfecta para amantes del senderismo y el turismo activo.")
                .pricePerNight(45.0)
                .location("Genave")
                .province(Province.JAEN)
                .maxGuests(4)
                .imageUrl("h8.jpg")
                .houseType(HouseType.CASA)
                .amenities(Set.of(wifi, parking,mascotas,cocina))
                .host(host3_booking)
                .build()
        );


        House house4_booking = houseRepository.save(House.builder()
                .title("Bungalow en Camping Resort Gavà")
                .description("Moderno bungalow a pie de playa con acceso a piscina comunitaria, ideal para unas vacaciones familiares.")
                .pricePerNight(105.0)
                .location("Gava")
                .province(Province.BARCELONA)
                .maxGuests(4)
                .imageUrl("h10.jpg")
                .houseType(HouseType.CASA)
                .amenities(Set.of( piscina, parking,mascotas,cocina))
                .host(host4_booking)
                .build()
        );
        houseRepository.save(house4_booking);

        House house5_booking = houseRepository.save(House.builder()
                .title("Ático Exclusivo con Terraza en el Centro")
                .description("Espectacular ático con gran terraza privada y vistas panorámicas, en pleno centro de la ciudad.")
                .pricePerNight(125.6)
                .location("Calle Madrid,8")
                .province(Province.ASTURIAS)
                .maxGuests(2)
                .imageUrl("h13.jpg")
                .houseType(HouseType.CASA)
                .amenities(Set.of(wifi, parking,mascotas,cocina))
                .host(host5_booking)
                .build()
        );
        houseRepository.save(house5_booking);

        House house6_booking = houseRepository.save(House.builder()
                .title("Ático Histórico en el Barrio de Santa Cruz")
                .description("Elegante ático reformado conservando el encanto tradicional sevillano, ubicado a minutos de la Giralda.")
                .pricePerNight(167.6)
                .location("Calle Herreros,9")
                .province(Province.SEVILLA)
                .maxGuests(6)
                .imageUrl("h3.jpg")
                .houseType(HouseType.CASA)
                .amenities(Set.of(wifi,parking,mascotas,cocina))
                .host(host5_booking)
                .build()
        );
        houseRepository.save(house6_booking);

        House house7_booking = houseRepository.save(House.builder()
                .title("Ático Dúplex Moderno con Vistas")
                .description("Amplio ático dúplex con todas las comodidades modernas, aire acondicionado y excelente comunicación.")
                .pricePerNight(280.6)
                .location("Plaza el Bierzo,9")
                .province(Province.BARCELONA)
                .maxGuests(4)
                .imageUrl("h12.jpg")
                .houseType(HouseType.CASA)
                .host(host5_booking)
                .build()
        );
        houseRepository.save(house7_booking);

        // Crear casa sin host asignado
        House h1 = houseRepository.save(House.builder()
                .title("Habitación Premium en el Centro de Valencia")
                .description("Habitación de diseño en piso compartido premium, con todos los servicios y excelente ambiente.")
                .pricePerNight(300.0)
                .location("Calle Principe Vergara 108")
                .province(Province.VALENCIA)
                .imageUrl("h11.jpg")
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
                .user(guest1_booking)
                .build();

        Review review2 = Review.builder()
                .comment("Nefasto")
                .house(house_test_booking)
                .title("HERMOSO LUGAR PARA IR EN FAMILIA")
                .rating(1)
                .user(guest2_booking)
                .build();

        Review review3 = Review.builder()
                .comment("Ni fu ni fa")
                .house(house1_booking)
                .title("Comí y no me morí")
                .rating(3)
                .user(guest3_booking)
                .build();

        Review review4 = Review.builder()
                .comment("Ni fu ni fa")
                .house(house2_booking)
                .title("Me pusieron de menos")
                .rating(2)
                .user(guest4_booking)
                .build();

        Review review5 = Review.builder()
                .comment("Excelente")
                .house(house3_booking)
                .title("Guay")
                .rating(5)
                .user(guest5_booking)
                .build();

        Review review6 = Review.builder()
                .comment("Una casa rural excelente, rodeada de senderos preciosos. Totalmente recomendable para desconectar de la ciudad.")
                .house(house3_booking)
                .title("Estancia perfecta en la naturaleza")
                .rating(5)
                .user(guest1_booking)
                .build();

        reviewRepository.saveAll(List.of(review1, review2, review3, review4, review5, review6));

        // Reserva pendiente

        LocalDateTime timeIn =  LocalDateTime.of(2026,7,10,12,0);
        LocalDateTime timeOut =  LocalDateTime.of(2026,7,15,12,0);
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
//        // Reserva pendiente (cancelada)
        LocalDateTime timeIn5 =  LocalDateTime.of(2026,5,10,12,0);
        LocalDateTime timeOut5 =  LocalDateTime.of(2026,5,15,12,0);
        Booking reserva6 = new Booking(guest1_booking,house2_booking,timeIn5,timeOut5);
        reserva6.cancelledBooking();
        bookingRepository.save(reserva6);
//
//        // Reserva de tipo confirmada
        LocalDateTime timeIn6 =  LocalDateTime.of(2026,8,15,12,0);
        LocalDateTime timeOut6 =  LocalDateTime.of(2026,8,20,12,0);
        Booking reserva7 = new Booking(guest1_booking,house3_booking,timeIn6,timeOut6);
        reserva7.confirmedBooking();
        bookingRepository.save(reserva7);

        // Favoritos para luis1 (guest1_booking)
        favoriteRepository.save(Favorite.builder().user(guest1_booking).house(house1).build());
        favoriteRepository.save(Favorite.builder().user(guest1_booking).house(house2_booking).build());

        // Recomendaciones recibidas por luis1
        houseRecommendedRepository.save(HouseRecommended.builder()
                .userFrom(guest2_booking)
                .userTo(guest1_booking)
                .houseRecommended(house2_booking)
                .message("Hola Luis, esta casa cerca de la playa te va a encantar para tus próximas vacaciones.")
                .timeRecommended(LocalDateTime.now().minusDays(2))
                .viewed(true)
                .build());

        // Recomendaciones enviadas por luis1
        houseRecommendedRepository.save(HouseRecommended.builder()
                .userFrom(guest1_booking)
                .userTo(guest2_booking)
                .houseRecommended(house3_booking)
                .message("Alba, mira esta casa rural en Jaén, es perfecta para hacer senderismo en otoño.")
                .timeRecommended(LocalDateTime.now().minusDays(1))
                .viewed(true)
                .build());

    }

}


