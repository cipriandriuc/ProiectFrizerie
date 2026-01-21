package com.example.frizerapp.controller;

import com.example.frizerapp.model.Appointment;
import com.example.frizerapp.model.Service;
import com.example.frizerapp.repository.AppointmentRepository;
import com.example.frizerapp.repository.ServiceRepository;
import org.springframework.web.bind.annotation.*;

import com.example.frizerapp.repository.BarberRepository;
import com.example.frizerapp.model.Barber;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.example.frizerapp.dto.AppointmentCreateRequestNested;



import java.time.LocalDateTime;
import java.util.List;



@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    private final AppointmentRepository appointmentRepository;
    private final ServiceRepository serviceRepository;
    private final BarberRepository barberRepository;


    public AppointmentController(AppointmentRepository appointmentRepository,
                                 ServiceRepository serviceRepository,
                                 BarberRepository barberRepository) {
        this.appointmentRepository = appointmentRepository;
        this.serviceRepository = serviceRepository;
        this.barberRepository = barberRepository;
    }
    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @PostMapping
    public Appointment addAppointment(@Valid @RequestBody AppointmentCreateRequestNested req) {

        Barber barber = barberRepository.findById(req.barber().id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Barber not found"));

        Service service = serviceRepository.findById(req.service().id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found"));

        LocalDateTime start = req.appointmentTime();
        LocalDateTime end = start.plusMinutes(service.getDuration());

        List<Appointment> overlappingAppointments =
                appointmentRepository.findByBarberIdAndAppointmentTimeBetween(
                        barber.getId(),
                        start,
                        end
                );

        if (!overlappingAppointments.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Barber is not available at this time. Choose another hour.");
        }

        Appointment appointment = new Appointment();
        appointment.setBarber(barber);
        appointment.setService(service);
        appointment.setAppointmentTime(start);
        appointment.setClientName(req.clientName());
        appointment.setPhoneNumber(req.phoneNumber());

        return appointmentRepository.save(appointment);
    }

}
