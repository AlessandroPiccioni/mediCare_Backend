package com.example.mediCare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mediCare.model.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
	
	//Cerca il medico tramite user_id chiave esterna di user
	Optional<Doctor> findByUserId(Long userId);
}
