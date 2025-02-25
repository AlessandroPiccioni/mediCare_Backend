package com.example.mediCare.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mediCare.model.Specialization;
import com.example.mediCare.repository.SpecializationRepository;

@RestController
@RequestMapping("/specialization")
@Validated
@CrossOrigin("*")
public class SpecializationController {
	
	@Autowired
	private SpecializationRepository specializationRepository;
	
	@GetMapping("/all")
	public List<Specialization> getAllSpecialization () {
	    return specializationRepository.findAll();
	}

}
