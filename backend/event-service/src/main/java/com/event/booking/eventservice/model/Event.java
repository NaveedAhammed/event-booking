package com.event.booking.eventservice.model;

import com.event.booking.eventservice.model.enums.AgeRestriction;
import com.event.booking.eventservice.model.enums.EventType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NonNull
    @Column(nullable = false)
    private UUID organizerId;

    @NonNull
    @Column(nullable = false)
    private String title;

    @NonNull
    @Column(nullable = false)
    private String description;

    @NonNull
    @Column(nullable = false)
    private Double entryFee;

    @NonNull
    @Column(nullable = false)
    private Integer capacity;

    @NonNull
    @Column(nullable = false)
    private LocalDate eventDate;

    @NonNull
    @Column(nullable = false)
    private LocalDateTime startTime;

    @NonNull
    @Column(nullable = false)
    private LocalDateTime endTime;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType eventType;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AgeRestriction ageRestriction;

    @NonNull
    @Column(nullable = false)
    private String location;

    @NonNull
    @Column(nullable = false)
    private Double latitude;

    @NonNull
    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PreUpdate
    private void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
