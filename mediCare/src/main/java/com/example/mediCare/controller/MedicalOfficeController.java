package com.example.mediCare.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mediCare.model.Doctor;
import com.example.mediCare.model.MedicalOffice;
import com.example.mediCare.model.Specialization;
import com.example.mediCare.model.User;
import com.example.mediCare.repository.MedicalOfficeRepository;
import com.example.mediCare.repository.SpecializationRepository;
import com.example.mediCare.service.MedicalOfficeService;

@RestController
@RequestMapping("/medicaloffice")
@Validated
@CrossOrigin("*")
public class MedicalOfficeController {
	
	@Autowired
	private MedicalOfficeRepository medicalOfficeRepository;
	
    @Autowired
    private MedicalOfficeService medicalOfficeService;
	
	@GetMapping("/all")
	public ResponseEntity<List<MedicalOffice>> getAllMedicalOffice () {
		List<MedicalOffice> medicalOffices=medicalOfficeRepository.findAll();
	    return ResponseEntity.ok(medicalOffices);
	}
	
	@GetMapping("/{nome}")
	public Optional<MedicalOffice> getMedicalOfficeByNome (@PathVariable String nome){
		return medicalOfficeRepository.findByNome(nome);
	}

	@GetMapping("/doctors/{id}")
    public HashMap<User, Doctor> getDoctorsWithUsersByMedicalOffice(@PathVariable Long id) {
        List<Doctor> doctors = medicalOfficeService.getDoctorsByMedicalOffice(id);
        
        // Creazione della HashMap User-Doctor
        HashMap<User, Doctor> userDoctorMap = new HashMap<>();
        for (Doctor doctor : doctors) {
            if (doctor.getUser() != null) { //Controllo per evitare NullPointerException
                userDoctorMap.put(doctor.getUser(), doctor);
            }
        }

        return userDoctorMap; //Restituisce la HashMap
    }
	
}
