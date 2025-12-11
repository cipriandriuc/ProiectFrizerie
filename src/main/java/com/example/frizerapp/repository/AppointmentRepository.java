package com.example.frizerapp.repository;

import com.example.frizerapp.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // găsește programările unui frizer într-un anumit interval de timp
    List<Appointment> findByBarberIdAndAppointmentTimeBetween(
            Long barberId, LocalDateTime start, LocalDateTime end
    );
}
