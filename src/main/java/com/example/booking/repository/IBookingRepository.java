package com.example.booking.repository;

import com.example.booking.model.BookingModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface IBookingRepository extends JpaRepository<BookingModel, UUID> {
    BookingModel findByEmail(String email);
}
