package com.example.frizerapp.repository;

import com.example.frizerapp.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Long> {
}
