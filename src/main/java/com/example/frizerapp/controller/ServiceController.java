package com.example.frizerapp.controller;

import com.example.frizerapp.model.Service;
import com.example.frizerapp.repository.ServiceRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "*") // permite accesul din browser sau alte aplicații
public class ServiceController {

    private final ServiceRepository serviceRepository;

    public ServiceController(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @GetMapping
    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    @PostMapping
    public Service addService(@RequestBody Service service) {
        return serviceRepository.save(service);
    }
}
