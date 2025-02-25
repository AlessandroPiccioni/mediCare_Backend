package com.example.mediCare.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mediCare.model.MedicalOffice;
import com.example.mediCare.model.Specialization;
import com.example.mediCare.repository.MedicalOfficeRepository;
import com.example.mediCare.repository.SpecializationRepository;

@RestController
@RequestMapping("/medicaloffice")
@Validated
@CrossOrigin("*")
public class MedicalOfficeController {
	
	@Autowired
	private MedicalOfficeRepository medicalOfficeRepository;
	
	@GetMapping("/all")
	public List<MedicalOffice> getAllMedicalOffice () {
	    return medicalOfficeRepository.findAll();
	}
	
	@GetMapping("/{nome}")
	public Optional<MedicalOffice> getMedicalOfficeByNome (@PathVariable String nome){
		System.out.println("aaaaaaaaaaaaaa");
		return medicalOfficeRepository.findByNome(nome);
	}
	
}
