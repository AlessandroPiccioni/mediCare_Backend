package com.example.mediCare.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mediCare.model.MedicalOffice;

@Repository
public interface MedicalOfficeRepository extends JpaRepository<MedicalOffice, Long> {
	
	//Cerca lo studio medico tramite il codice fiscale
	Optional<MedicalOffice> findByCodiceFiscale(String codiceFiscale);
	
	//Cerca lo studio medico tramite il nome e il codice fiscale
	Optional<MedicalOffice> findByCodiceFiscaleAndNome(String codiceFiscale, String nome);
	
	
	
}
