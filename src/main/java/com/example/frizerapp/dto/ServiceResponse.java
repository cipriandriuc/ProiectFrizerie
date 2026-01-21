package com.example.frizerapp.dto;

public record ServiceResponse(
        Long id,
        String name,
        Double price,
        Integer duration
) {}