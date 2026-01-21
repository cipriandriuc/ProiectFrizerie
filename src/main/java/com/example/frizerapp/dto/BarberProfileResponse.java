package com.example.frizerapp.dto;

import java.util.List;


public record BarberProfileResponse(
        Long id,
        String name,
        String phone,
        String email,
        String description,
        List<ServiceResponse> services
) {}
