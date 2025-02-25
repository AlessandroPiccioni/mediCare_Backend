package com.example.mediCare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class DoctorSpecialization {
	
	//Chiave primaria
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	Long id;
	
	//Relazione M-M tra doctor e specialization
	
	@ManyToOne
	@JoinColumn (name="doctor_id")
	private Doctor doctor;
	
	@ManyToOne
	@JoinColumn (name="specialization_id")
	private Specialization specialization;
	
	//Costruttore di default
	public DoctorSpecialization() {
	}
	
	//getters e setters
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public Specialization getSpecialization() {
		return specialization;
	}

	public void setSpecialization(Specialization specialization) {
		this.specialization = specialization;
	}
	
	
	

}
