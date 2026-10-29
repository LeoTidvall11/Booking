package com.example.booking.service;

import com.example.booking.model.BookingModel;
import com.example.booking.repository.IBookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private final IBookingRepository bookingRepository;

    public BookingService(IBookingRepository bookingRepository){
        this.bookingRepository = bookingRepository;
    }


    public BookingModel createBooking(BookingModel booking) {
        return bookingRepository.save(booking);
    }

    public List<BookingModel> getAllBookings() {
        return bookingRepository.findAll();
    }
}
