package com.example.frizerapp.repository;

import com.example.frizerapp.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByBarberId(Long barberId);
}
