package com.example.mediCare.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Specialization {
	
	public static List<String> fields = new ArrayList<>();
	
	//Metodo che si avvia quando la classe va in memoria
	static {
	    fields.add("Patient Name");
		fields.add("Date of Birth");
		fields.add("Medical Record Number");
		fields.add("Allergies");
		fields.add("Medications");
		fields.add("Diagnosis");
		fields.add("Treatment Plan");
		fields.add("Physician Name");
		fields.add("Appointment Date");    
		fields.add("Insurance Provider");
	}
	
	//Chiave primaria
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	Long id;
	
	//Crea uan relazione con la tabella Doctor
	//Un dottore puo avere piu specializzazioni
	//una stessa specializzazione puo essere posseduta da piu dottori
    @OneToMany
    @JoinColumn (name="specialization_id")
    @JsonIgnore
	private List<DoctorSpecialization> doctorSpecialization;
	
	//Il campo di studio
	@NotBlank(message = "Nome utente è obbligatorio")
	private String field;
	
	//Costruttore di default
	public Specialization() {
	}
	
	//getters e setters
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<DoctorSpecialization> getDoctorSpecialization() {
		return doctorSpecialization;
	}

	public void setDoctorSpecialization(List<DoctorSpecialization> doctorSpecialization) {
		this.doctorSpecialization = doctorSpecialization;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
	
	
	

}
