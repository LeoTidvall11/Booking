package com.example.booking.controller;

import com.example.booking.model.BookingModel;
import com.example.booking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bookings")
public class BookingController {



    private final BookingService bookingService;

    public BookingController(BookingService bookingservice) {
        this.bookingService = bookingservice;
    }

    @PostMapping
    public BookingModel createBooking(@Valid @RequestBody BookingModel booking) {
        return bookingService.createBooking(booking);
    }

    @GetMapping
    public List<BookingModel> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    public BookingModel getBookingById(@PathVariable("id") UUID id) {
        return bookingService.getBookingById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookingById(@PathVariable UUID id){
        bookingService.deleteBookingById(id);

        return ResponseEntity.noContent().build();

    }

    @PutMapping("/{id}")
    public BookingModel updateBooking(@PathVariable UUID id, @Valid @RequestBody BookingModel updatedBooking){
        return bookingService.updateBooking(id, updatedBooking);
    }
}
