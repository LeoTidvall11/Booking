package com.example.booking.service;

import com.example.booking.model.BookingModel;
import com.example.booking.model.BookingStatus;
import com.example.booking.repository.IBookingRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class BookingService {



    private final IBookingRepository bookingRepository;

    public BookingService(IBookingRepository bookingRepository){
        this.bookingRepository = bookingRepository;
    }


    public BookingModel createBooking(BookingModel booking) {
        if (booking.getStatus() == null) {
            booking.setStatus(BookingStatus.PENDING);
        }
        return bookingRepository.save(booking);
    }

    public List<BookingModel> getAllBookings() {
        return bookingRepository.findAll(Sort.by(Sort.Direction.DESC,"createdAt"));
    }

    public BookingModel getBookingById (UUID id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
    }

    public void deleteBookingById(UUID id){
        if(!bookingRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found");
        }
        bookingRepository.deleteById(id);
    }

    public BookingModel findByEmail(String email){
        return bookingRepository.findByEmail(email);
    }

    public BookingModel updateBooking(UUID id,BookingModel updatedBooking){
        BookingModel existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));

        existingBooking.setName(updatedBooking.getName());
        existingBooking.setEmail(updatedBooking.getEmail());
        existingBooking.setPhoneNumber(updatedBooking.getPhoneNumber());
        existingBooking.setNumberOfPeople(updatedBooking.getNumberOfPeople());
        existingBooking.setDate(updatedBooking.getDate());
        if (updatedBooking.getStatus() != null) {
            existingBooking.setStatus(updatedBooking.getStatus());
        }

        return bookingRepository.save(existingBooking);
    }
}
