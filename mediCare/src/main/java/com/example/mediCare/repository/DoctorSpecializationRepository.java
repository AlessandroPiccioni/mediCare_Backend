package com.example.mediCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mediCare.model.DoctorSpecialization;

@Repository
public interface DoctorSpecializationRepository extends JpaRepository<DoctorSpecialization, Long>{
	
	//Crea una relazione con lo stesso id medico con tutte gli id specializzazione di una lista
	//void saveAllWithDoctorId(Long doctorId, Iterable<Long> specializationIds);

}
