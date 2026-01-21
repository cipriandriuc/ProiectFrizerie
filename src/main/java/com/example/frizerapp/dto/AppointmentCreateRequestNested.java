package com.example.frizerapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AppointmentCreateRequestNested(
        @NotBlank String clientName,
        @NotBlank String phoneNumber,
        @NotNull LocalDateTime appointmentTime,
        @NotNull IdOnly barber,
        @NotNull IdOnly service
) {
    public record IdOnly(@NotNull Long id) {}
}
