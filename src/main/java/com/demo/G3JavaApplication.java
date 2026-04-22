package com.demo;

import com.demo.model.Booking;
import com.demo.model.User;
import com.demo.repository.BookingRepository;
import com.demo.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.print.Book;

@SpringBootApplication
public class G3JavaApplication {

    public static void main(String[] args) {
        //SpringApplication.run(G3JavaApplication.class, args);
        var context = SpringApplication.run(G3JavaApplication.class, args);

        BookingRepository repositoryBooking =context.getBean(BookingRepository.class);
        Booking bookingTest = new Booking(1L);
        repositoryBooking.save(bookingTest);



    }

}
