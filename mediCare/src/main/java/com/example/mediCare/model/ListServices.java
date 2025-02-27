package com.example.mediCare.model;

import jakarta.persistence.*;

@Entity
@Table(name = "prestazioni")
public class ListServices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descrizione;
    private String medico;
    private String disponibilita;

    @ManyToOne
    @JoinColumn(name = "medical_office_id")
    private MedicalOffice medicalOffice;
    // Costruttori
    public ListServices() {}

    public ListServices(String nome, String descrizione, String medico, String disponibilita, MedicalOffice medicalOffice) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.medico = medico;
        this.disponibilita = disponibilita;
        this.medicalOffice = medicalOffice;  // Inizializzazione del collegamento con lo studio medico
    }

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

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getMedico() {
		return medico;
	}

	public void setMedico(String medico) {
		this.medico = medico;
	}

	public String getDisponibilita() {
		return disponibilita;
	}

	public void setDisponibilita(String disponibilita) {
		this.disponibilita = disponibilita;
	}

	public MedicalOffice getMedicalOffice() {
		return medicalOffice;
	}

	public void setMedicalOffice(MedicalOffice medicalOffice) {
		this.medicalOffice = medicalOffice;
	}

   
    }

