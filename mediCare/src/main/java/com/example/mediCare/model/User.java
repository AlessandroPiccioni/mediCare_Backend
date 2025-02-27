package com.example.mediCare.model;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.persistence.OneToOne;

@Entity
public class User {
	
	//Stabiliamo tutte le tipologie di accesso agli endpoint
	public static enum Ruolo {Paziente, Medico, Admin};
	
	//Chiave primaria 
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	//Relazione onetoone con la tabella Doctor
	//Un utente puo essere solo un medico
	//un medico puo essere soltanto un utente
	@OneToOne
	@JoinColumn(name = "user_id")
	private Doctor doctor;
	
	//Il nome dell'utente
	@NotBlank(message = "Nome utente è obbligatorio")
	private String nome;
	
	//Il cognome dell'utente
	@NotBlank(message = "Nome utente è obbligatorio")
	private String cognome;
	
	@NotBlank(message = "Nome utente è obbligatorio")
	@Size(min = 16, max = 16, message = "Il codice fiscale deve essere lungo 16 caratteri")
    @Pattern(regexp = "^[A-Z0-9]{16}$", message = "Il codice fiscale deve contenere solo lettere maiuscole e numeri")
	@Column(unique = true)
	private String codiceFiscale;
	
	//Email dell'utente
	@NotBlank(message = "Nome utente è obbligatorio")
    @Email(message = "Formato email non valido")
	@Column(unique = true)
    private String email;
    
	//Password dell'utente
    @NotNull(message = "Password è obbligatoria")
    @Length(min = 10, max = 30, message = "Password deve essere minimo di 10 e 30 massimo caratteri")
	private String password;
  
    //Data di nascita dell'utente
	@Past(message = "Data non valida")
    @NotNull(message = "Data di nascita è obbligatoria")
    private LocalDate dataNascita;
	
	//Ruolo
    private Ruolo ruolo;
	
    //token per mantenere il login
	@Column(unique = true)
	private String token;
	
	//Costruttore di default
	public User() {
	}
	
	//getters e setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDate getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
	}

	public Ruolo getRuolo() {
		return ruolo;
	}

	public void setRuolo(Ruolo ruolo) {
		this.ruolo = ruolo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	
	
	
	

}
