package com.event.booking.eventservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRequestDTO {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Entry fee is required")
    @Min(value = 1, message = "Entry fee should be at least 1")
    private Double entryFee;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity should be at least 1")
    private Integer capacity;

    @NotNull(message = "Event date is required")
    @Future(message = "Event date must be in the future")
    private LocalDate eventDate;

    @NotNull(message = "Event start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "Event end time is required")
    private LocalDateTime endTime;

    @NotBlank(message = "Event type is required")
    private String eventType;

    @NotBlank(message = "Age restriction is required")
    private String ageRestriction;

    @NotBlank(message = "Event location is required")
    private String location;

    @NotNull(message = "Event latitude is required")
    private Double latitude;

    @NotNull(message = "Event longitude is required")
    private Double longitude;
}
