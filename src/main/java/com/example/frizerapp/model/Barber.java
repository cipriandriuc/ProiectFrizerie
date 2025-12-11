package com.example.frizerapp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Barber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;        // numele frizerului
    private String phone;       // telefon frizer
    private String email;       // optional
    private String description; // ex: "Specializat în tunsori fade"
}
