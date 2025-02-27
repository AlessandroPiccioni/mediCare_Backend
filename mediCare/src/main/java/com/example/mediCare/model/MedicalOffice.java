package com.example.mediCare.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class MedicalOffice {
	
	//Chiave primaria
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	Long id;
	
	//Crea uan relazione con la tabella MedicalOffice
	//Un dottore puo essere parte di un solo studio medico
	//Uno studio medico puo avere piu dottori
    @OneToMany(mappedBy = "medicalOffice")
	private List <Doctor> doctor;
	
	@NotBlank(message = "Nome utente è obbligatorio")
	@Column(unique = true)
	private String nome;
	
	@NotBlank(message = "Nome utente è obbligatorio")
	@Size(min = 16, max = 16, message = "Il codice fiscale deve essere lungo 16 caratteri")
    @Pattern(regexp = "^[A-Z0-9]{16}$", message = "Il codice fiscale deve contenere solo lettere maiuscole e numeri")
	@Column(unique = true)
	private String codiceFiscale;
	
	//Costruttore di default
	public MedicalOffice() {
	}
	
	//getters e setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Doctor> getDoctor() {
		return doctor;
	}

	public void setDoctor(List<Doctor> doctor) {
		this.doctor = doctor;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	
	

}
