package com.example.booking.controller;

import com.example.booking.model.BookingModel;
import com.example.booking.service.BookingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {



    private final BookingService bookingService;

    public BookingController(BookingService bookingservice) {
        this.bookingService = bookingservice;
    }

    @PostMapping
    public BookingModel createBooking(@RequestBody BookingModel booking) {
        return bookingService.createBooking(booking);
    }

    @GetMapping
    public List<BookingModel> getAllBookings() {
        return bookingService.getAllBookings();
    }
}
