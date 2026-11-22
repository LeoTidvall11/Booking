package com.example.booking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @NotBlank(message = "name måste anges")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "email måste anges")
    @Email(message = "email har fel format")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "phoneNumber måste anges")
    private String phoneNumber;

    @Column(nullable = false)
    @Min(value = 1, message = "numberOfPeople måste vara minst 1")
    private short numberOfPeople;

    @Column(nullable = false)
    @NotNull(message = "date måste anges")
    private LocalDateTime date;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)

    private BookingStatus status;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
    }


}
