package com.example.mediCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mediCare.model.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

}
