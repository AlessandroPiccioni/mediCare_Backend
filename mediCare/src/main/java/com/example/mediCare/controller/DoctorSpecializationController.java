package com.example.mediCare.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mediCare.model.Doctor;
import com.example.mediCare.model.DoctorSpecialization;
import com.example.mediCare.model.MedicalOffice;
import com.example.mediCare.model.Specialization;
import com.example.mediCare.model.User;
import com.example.mediCare.repository.DoctorRepository;
import com.example.mediCare.repository.DoctorSpecializationRepository;
import com.example.mediCare.repository.SpecializationRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/doctorspecialization")
@Validated
@CrossOrigin("*")
public class DoctorSpecializationController {
	
	@Autowired
	private DoctorSpecializationRepository doctorSpecializationRepository;
	
	@Autowired
	private DoctorRepository doctorRepository;
	
	@Autowired
	private SpecializationRepository specializationRepository;
	
	@GetMapping("/search/{search}")
	public ResponseEntity<Object> getAllSpecialization(@PathVariable("search") String search, HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("Sono entrato qui dentro");
		
		//Prendo tutti i dottori e le specializzazioni
	    List<Doctor> doctors = doctorRepository.findAll();
	    List<Specialization> specializations = specializationRepository.findAll();
	    
	    System.out.println("Ho presso tutte le specializzazioni e i dottori");
	    
	    search = search.trim();
	    
	    //Controlla se la ricerca ci sia
	    if (search == null || search.isEmpty()) {
	        return ResponseEntity.ok(doctors); 
	    }

	    System.out.println("Ho controllato se la ricerca non si vuota");
	    
	    List<User> resultUser = new ArrayList<>();
	    List<Specialization> resultSpecializations = new ArrayList<>();
	    
	    System.out.println("Inizio la ricerca dei dottori");
	    
	    //Cerca se i dottori corrispondo con il campo di ricerca
	    for (Doctor doctor : doctors) {
	        String fullName = String.format("%s %s",doctor.getUser().getCognome(), doctor.getUser().getNome());        
	        
	        if (fullName.toLowerCase().contains(search.toLowerCase())) {
	            resultUser.add(doctor.getUser());
	        }
	    }
	    
	    //Se la lista non è vuota ritorna i dottori 
	    if (!resultUser.isEmpty()) {
	        return ResponseEntity.ok(resultUser);
	    }
	    
	    System.out.println("Inizio la ricerca delle specializzazioni");

	  //Cerca le specializzazioni che corrispondono con il campo di ricerca
	    for (Specialization specialization : specializations) {
	        if (specialization.getField().contains(search)) { 
	            resultSpecializations.add(specialization);
	        }
	    }
	    
	    System.out.println("Controllo se le specializzazioni sono salvate nel databse");
	    
	   //Se la lista non è vuota ritorna le specializzazioni
	    if (!resultSpecializations.isEmpty()) {
	        return ResponseEntity.ok(resultSpecializations);
	    }
	    
	    System.out.println("Non ho trovato niente");

	    //Non ha trovato niente 
	    return ResponseEntity.noContent().build();
	}

	

}
