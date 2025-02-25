package com.example.mediCare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mediCare.model.Doctor;
import com.example.mediCare.model.DoctorSpecialization;
import com.example.mediCare.model.Specialization;

@Repository
public interface DoctorSpecializationRepository extends JpaRepository<DoctorSpecialization, Long>{
	

}
