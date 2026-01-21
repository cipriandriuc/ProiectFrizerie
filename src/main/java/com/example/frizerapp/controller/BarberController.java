package com.example.frizerapp.controller;

import com.example.frizerapp.model.Appointment;
import com.example.frizerapp.model.Barber;
import com.example.frizerapp.model.Service;
import com.example.frizerapp.repository.AppointmentRepository;
import com.example.frizerapp.repository.BarberRepository;
import com.example.frizerapp.repository.ServiceRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.example.frizerapp.dto.BarberProfileResponse;
import com.example.frizerapp.dto.ServiceResponse;


@RestController
@RequestMapping("/api/barbers")
@CrossOrigin(origins = "*")
public class BarberController {

    private final BarberRepository barberRepository;
    private final AppointmentRepository appointmentRepository;
    private final ServiceRepository serviceRepository;

    public BarberController(BarberRepository barberRepository,
                            AppointmentRepository appointmentRepository,
                            ServiceRepository serviceRepository) {
        this.barberRepository = barberRepository;
        this.appointmentRepository = appointmentRepository;
        this.serviceRepository = serviceRepository;
    }

    // Lista tuturor frizerilor (recomand sa o pastrezi)
    @GetMapping
    public List<Barber> getAllBarbers() {
        return barberRepository.findAll();
    }

    // Availability: ore libere pentru frizer intr-o zi, pentru un anumit serviciu
    @GetMapping("/{barberId}/availability")
    public List<String> getAvailability(
            @PathVariable Long barberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam Long serviceId
    ) {
        // 1) durata serviciului selectat
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found with id " + serviceId));

        int durationMinutes = service.getDuration();

        // 2) programarile frizerului din ziua respectiva
        LocalDateTime dayStart = date.atStartOfDay();
        LocalDateTime dayEnd = date.atTime(LocalTime.MAX);

        List<Appointment> appointments =
                appointmentRepository.findByBarberIdAndAppointmentTimeBetween(barberId, dayStart, dayEnd);

        // 3) interval salon + sloturi
        LocalTime open = LocalTime.of(10, 0);
        LocalTime close = LocalTime.of(18, 0);
        int stepMinutes = 30;

        List<String> available = new ArrayList<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");

        LocalDateTime slotStart = LocalDateTime.of(date, open);
        LocalDateTime lastStart = LocalDateTime.of(date, close).minusMinutes(durationMinutes);

        while (!slotStart.isAfter(lastStart)) {
            //LocalDateTime slotEnd = slotStart.plusMinutes(durationMinutes);
            final LocalDateTime currentStart = slotStart;
            final LocalDateTime currentEnd = currentStart.plusMinutes(durationMinutes);
            boolean overlaps = appointments.stream().anyMatch(a -> {
                LocalDateTime existingStart = a.getAppointmentTime();
                int existingDuration = a.getService().getDuration();
                LocalDateTime existingEnd = existingStart.plusMinutes(existingDuration);

                return currentStart.isBefore(existingEnd) && currentEnd.isAfter(existingStart);
                // suprapunere: start < otherEnd && end > otherStart
                //return slotStart.isBefore(existingEnd) && slotEnd.isAfter(existingStart);
            });

            if (!overlaps) {
                available.add(currentStart.toLocalTime().format(fmt));
            }

            slotStart = slotStart.plusMinutes(stepMinutes);
        }

        return available;
    }

    // Adauga un frizer
    @PostMapping
    public Barber addBarber(@RequestBody Barber barber) {
        return barberRepository.save(barber);
    }

    // Detalii frizer
    @GetMapping("/{id}")
    public Barber getBarberById(@PathVariable Long id) {
        return barberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barber not found with id " + id));
    }
    // Profile endpoint (fara poze): barber + servicii lui intr-un singur JSON
    @GetMapping("/{id}/profile")
    public BarberProfileResponse getBarberProfile(@PathVariable Long id) {
        Barber barber = barberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barber not found with id " + id));

        List<ServiceResponse> services = serviceRepository.findByBarberId(id)
                .stream()
                .map(s -> new ServiceResponse(
                        s.getId(),
                        s.getName(),
                        s.getPrice(),
                        s.getDuration()
                ))
                .toList();

        return new BarberProfileResponse(
                barber.getId(),
                barber.getName(),
                barber.getPhone(),
                barber.getEmail(),
                barber.getDescription(),
                services
        );
    }

}
