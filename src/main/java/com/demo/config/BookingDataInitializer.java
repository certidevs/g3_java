package com.demo.config;

import com.demo.model.Booking;
import com.demo.model.House;
import com.demo.model.StatusReserva;
import com.demo.model.User;
import com.demo.repository.BookingRepository;
import com.demo.repository.HouseRepository;
import com.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

//
//@Component
//@Profile("!test")
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

        //////////////// Datos verificados en las relaciones

        // Guests
        User guest_test_booking = new User();
        guest_test_booking.setUsername("Guest prueba");
        guest_test_booking.setFirstName("Guest 1");
        guest_test_booking.setLastName("Guest 1");
        guest_test_booking.setEmail("guest1@test1.com");
        userRepository.save(guest_test_booking);

        User guest_test_booking_1 = new User();
        guest_test_booking_1.setUsername("Guest prueba 2");
        guest_test_booking_1.setFirstName("Guest 2");
        guest_test_booking_1.setLastName("Guest 2");
        guest_test_booking_1.setEmail("guest2@test1.com");
        userRepository.save(guest_test_booking_1);


        // Host
        User host_test_booking = new User();
        host_test_booking.setUsername("Host prueba");
        host_test_booking.setFirstName("Host 1");
        host_test_booking.setLastName("Host 1");
        host_test_booking.setEmail("host1@test1.com");
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
                .host(host_test_booking)
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

        // user host
        // casa

        // user guest
        // booking

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


        // Crear usuarios (guest)
        User guest1_booking = new User();
        guest1_booking.setUsername("luis1");
        guest1_booking.setFirstName("Luis");
        guest1_booking.setLastName("López");
        guest1_booking.setEmail("luis@test1.com");
        userRepository.save(guest1_booking);

        User guest2_booking = new User();
        guest2_booking.setUsername("alba2");
        guest2_booking.setFirstName("Alba");
        guest2_booking.setLastName("Martínez");
        guest2_booking.setEmail("alba@test2.com");
        userRepository.save(guest2_booking);

        User guest3_booking = new User();
        guest3_booking.setUsername("jose3");
        guest3_booking.setEmail("jose@test3.com");
        userRepository.save(guest3_booking);

        User guest4_booking = new User();
        guest4_booking.setUsername("carlos4");
        guest4_booking.setEmail("carlos@4test.com");
        userRepository.save(guest4_booking);

        User guest5_booking = new User();
        guest5_booking.setUsername("pedro5");
        guest5_booking.setEmail("pedro@5test.com");
        userRepository.save(guest5_booking);

        // Crear usuarios (guest)
        User host1_booking = new User();
        host1_booking.setUsername("luis6");
        host1_booking.setEmail("luis@test6.com");
        userRepository.save(host1_booking);

        User host2_booking = new User();
        host2_booking.setUsername("alba7");
        host2_booking.setEmail("alba@test7.com");
        userRepository.save(host2_booking);

        User host3_booking = new User();
        host3_booking.setUsername("jose8");
        host3_booking.setEmail("jose@test.8com");
        userRepository.save(host3_booking);

        User host4_booking = new User();
        host4_booking.setUsername("carlos9");
        host4_booking.setEmail("carlos@test9.com");
        userRepository.save(host4_booking);

        User host5_booking = new User();
        host5_booking.setUsername("pedro10");
        host5_booking.setEmail("pedro@test10.com");
        userRepository.save(host5_booking);

        House house1_booking = houseRepository.save(House.builder()
                        .title("Parcela el Viso")
                        .description("con piscina")
                        .pricePerNight(145.0)
                        .location("Toledo")
                        .province("Madrid")
                        .maxGuests(5)
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
                .host(host2_booking)
                .build()
        );

//
        House house3_booking = houseRepository.save(House.builder()
                .title("Rural Torres")
                .description("senderismo sierra")
                .pricePerNight(45.0)
                .maxGuests(4)
                .host(host3_booking)
                .build()
        );


        House house4_booking = houseRepository.save(House.builder()
                .title("Camping")
                .description("en playa con piscina")
                .pricePerNight(105.0)
                .maxGuests(4)
                .host(host4_booking)
                .build()
        );



        House house5_booking = houseRepository.save(House.builder()
                .title("Ático")
                .description("centro ciudad")
                .pricePerNight(250.6)
                .province("Gijón")
                .maxGuests(2)
                .host(host5_booking)
                .build()
        );

        House house6_booking = houseRepository.save(House.builder()
                .title("Ático1")
                .description("centro ciudad1")
                .pricePerNight(167.6)
                .province("Gijón1")
                .maxGuests(6)
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
                .host(host5_booking)
                .build()
        );
        houseRepository.save(house7_booking);

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


