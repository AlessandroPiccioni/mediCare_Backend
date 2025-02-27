package com.example.mediCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.mediCare.model.ListServices;

public interface ListServicesRepository extends JpaRepository<ListServices, Long> {
    // JpaRepository fornisce già metodi come saveAll
    // non c'è bisogno di scrivere un saveAll personalizzato
}
