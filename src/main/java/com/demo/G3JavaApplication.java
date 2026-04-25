package com.demo;

import com.demo.model.Booking;
import com.demo.repository.BookingRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class G3JavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(G3JavaApplication.class, args);
        /*var context = SpringApplication.run(G3JavaApplication.class, args);

        BookingRepository repositoryBooking =context.getBean(BookingRepository.class);
        // Se crea reserva de USUARIO y CASA ficticio.
        Booking bookingTest = new Booking(1L,1L);
        repositoryBooking.save(bookingTest);
        */
    }

}
