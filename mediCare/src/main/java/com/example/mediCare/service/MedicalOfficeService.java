package com.example.mediCare.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mediCare.model.Doctor;
import com.example.mediCare.model.MedicalOffice;
import com.example.mediCare.repository.MedicalOfficeRepository;

@Service
public class MedicalOfficeService {

    @Autowired
    private MedicalOfficeRepository medicalOfficeRepository;

    public List<Doctor> getDoctorsByMedicalOffice(Long id) {
        MedicalOffice medicalOffice = medicalOfficeRepository.findById(id).orElseThrow(() -> new RuntimeException("Studio medico non trovato"));
        return medicalOffice.getDoctor();
    }
    
}
