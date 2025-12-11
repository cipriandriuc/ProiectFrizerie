package com.example.frizerapp.controller;

import com.example.frizerapp.model.Barber;
import com.example.frizerapp.repository.BarberRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barbers")
@CrossOrigin(origins = "*")
public class BarberController {

    private final BarberRepository barberRepository;

    public BarberController(BarberRepository barberRepository) {
        this.barberRepository = barberRepository;
    }

    //  Lista tuturor frizerilor
    @GetMapping
    public List<Barber> getAllBarbers() {
        return barberRepository.findAll();
    }

    //  Adauga un frizer nou
    @PostMapping
    public Barber addBarber(@RequestBody Barber barber) {
        return barberRepository.save(barber);
    }

    //  Detalii despre un frizer anume
    @GetMapping("/{id}")
    public Barber getBarberById(@PathVariable Long id) {
        return barberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barber not found with id " + id));
    }
}

