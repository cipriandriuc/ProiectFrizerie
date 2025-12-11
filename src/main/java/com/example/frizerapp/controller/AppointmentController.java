package com.example.frizerapp.controller;

import com.example.frizerapp.model.Appointment;
import com.example.frizerapp.model.Service;
import com.example.frizerapp.repository.AppointmentRepository;
import com.example.frizerapp.repository.ServiceRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    private final AppointmentRepository appointmentRepository;
    private final ServiceRepository serviceRepository;

    public AppointmentController(AppointmentRepository appointmentRepository,
                                 ServiceRepository serviceRepository) {
        this.appointmentRepository = appointmentRepository;
        this.serviceRepository = serviceRepository;
    }

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @PostMapping
    public Object addAppointment(@RequestBody Appointment appointment) {

        // 1. Luăm serviciul ca să știm durata
        Service service = serviceRepository.findById(appointment.getService().getId())
                .orElseThrow(() -> new RuntimeException("Service not found"));

        int duration = service.getDuration(); // durata serviciului în minute

        // 2. Calculăm intervalul de timp al programării
        LocalDateTime start = appointment.getAppointmentTime();
        LocalDateTime end = start.plusMinutes(duration);

        // 3. Verificăm dacă frizerul e liber
        List<Appointment> overlappingAppointments =
                appointmentRepository.findByBarberIdAndAppointmentTimeBetween(
                        appointment.getBarber().getId(),
                        start,
                        end
                );

        if (!overlappingAppointments.isEmpty()) {
            return "Barber is not available at this time. Choose another hour.";
        }

        // 4. Salvează programarea
        return appointmentRepository.save(appointment);
    }
}
