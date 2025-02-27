package com.example.mediCare.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Doctor {
	
	//Chaive Primaria
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	Long id;
	
	//Relazione onetoone con la tabella Doctor
	//Un utente puo essere solo un medico
	//un medico puo essere soltanto un utente
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	//Crea uan relazione con la tabella specialization
	//Un dottore puo avere piu specializzazioni
	//una stessa specializzazione puo essere posseduta da piu dottori
	@OneToMany
	@JoinColumn (name="doctor_id")
	private List<DoctorSpecialization> doctorSpecialization;
	
	//Crea uan relazione con la tabella MedicalOffice
	//Un dottore puo essere parte di un solo studio medico
	//Uno studio medico puo avere piu dottori
	@ManyToOne
	@JoinColumn (name="medicalOffice_id")
	private MedicalOffice medicalOffice;
	
	//Costruttore di defualt
	public Doctor () {
	}
	
	//getters e setters
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<DoctorSpecialization> getDoctorSpecialization() {
		return doctorSpecialization;
	}

	public void setDoctorSpecialization(List<DoctorSpecialization> doctorSpecialization) {
		this.doctorSpecialization = doctorSpecialization;
	}

	public MedicalOffice getMedicalOffice() {
		return medicalOffice;
	}

	public void setMedicalOffice(MedicalOffice medicalOffice) {
		this.medicalOffice = medicalOffice;
	}
	
	
	

}
