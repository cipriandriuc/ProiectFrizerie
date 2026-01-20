package com.example.frizerapp;

import com.example.frizerapp.model.Barber;
import com.example.frizerapp.model.Service;
import com.example.frizerapp.repository.BarberRepository;
import com.example.frizerapp.repository.ServiceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final BarberRepository barberRepository;
    private final ServiceRepository serviceRepository;

    public DataLoader(BarberRepository barberRepository, ServiceRepository serviceRepository) {
        this.barberRepository = barberRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Verificăm dacă baza e goală ca să nu dublăm datele la fiecare restart
        if (barberRepository.count() == 0) {

            // 1. Creăm Frizerul
            Barber ion = new Barber();
            ion.setName("Ion Popescu");
            ion.setDescription("Specialist în tunsori clasice și moderne.");
            ion.setEmail("ion@frizerie.ro");
            ion.setPhone("0722000000");

            // Salvăm frizerul în baza de date
            barberRepository.save(ion);
            System.out.println("DEBUG: Am salvat frizerul Ion (ID " + ion.getId() + ")");

            // 2. Creăm Serviciul 1 (Tuns)
            Service tuns = new Service();
            tuns.setName("Tuns Clasic");
            tuns.setPrice(50.0);
            tuns.setDuration(30); // minute
            tuns.setBarber(ion); // Îl legăm de Ion

            serviceRepository.save(tuns);

            // 3. Creăm Serviciul 2 (Barbă)
            Service barba = new Service();
            barba.setName("Aranjat Barbă");
            barba.setPrice(30.0);
            barba.setDuration(15);
            barba.setBarber(ion); // Îl legăm tot de Ion

            serviceRepository.save(barba);

            System.out.println("DEBUG: Datele au fost populate cu succes!");
        }
    }
}