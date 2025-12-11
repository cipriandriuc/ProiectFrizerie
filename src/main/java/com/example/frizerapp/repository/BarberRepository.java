package com.example.frizerapp.repository;

import com.example.frizerapp.model.Barber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarberRepository extends JpaRepository<Barber, Long> {
}
