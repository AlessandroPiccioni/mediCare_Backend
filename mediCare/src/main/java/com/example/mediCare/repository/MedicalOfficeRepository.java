package com.example.mediCare.repository;

import com.example.mediCare.model.MedicalOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalOfficeRepository extends JpaRepository<MedicalOffice, Long> {
    List<MedicalOffice> findByNomeContainingIgnoreCase(String nome);
}
