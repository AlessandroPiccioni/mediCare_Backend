package com.example.mediCare.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mediCare.auth.TokenService;
import com.example.mediCare.model.Doctor;
import com.example.mediCare.model.DoctorSpecialization;
import com.example.mediCare.model.MedicalOffice;
import com.example.mediCare.model.Specialization;
import com.example.mediCare.model.User;
import com.example.mediCare.repository.DoctorRepository;
import com.example.mediCare.repository.DoctorSpecializationRepository;
import com.example.mediCare.repository.MedicalOfficeRepository;
import com.example.mediCare.repository.SpecializationRepository;
import com.example.mediCare.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/doctor")
@Validated
@CrossOrigin("*")
public class DoctorController {
	
	@Autowired
	private DoctorRepository doctorRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MedicalOfficeRepository medicalOfficeRepository;
	
	@Autowired
	private DoctorSpecializationRepository doctorSpecializationRepository;
	
	@Autowired
	private SpecializationRepository specializationRepository;
	
    @Autowired
    private TokenService tokenService;
    
    /**
     * Endpoint per la registrazione dell'utente al sito
     * 
     * @param utente		Utente da registrare
     * @param request		Oggetto HttpServletRequest che contiene informazioni della richietsa
     * @param response		Oggetto HttpServletResponse che contiene informazioni della risposta 
     * @return ritorna la risposta dell'endpint composta da httpstatus e dal nuovo oggetto User
     */
    /*
    @PostMapping 
    public Object createUtenteUser(@RequestBody User user, @PathVariable String codiceFiscale, @PathVariable String nome, @PathVariable List<String> nomeSpecializzazione,HttpServletRequest request, HttpServletResponse response) {
    	//Cerca se l'utente eiste
    	if(userRepository.findByEmailAndPasswordAndCodiceFiscale(user.getEmail(), user.getPassword(), user.getCodiceFiscale()).isPresent()) {
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    	Optional<MedicalOffice> opMedicalOffice =medicalOfficeRepository.findByCodiceFiscaleAndNome(codiceFiscale, nome);
    	//Controlla se lo studio medico ricercato esista
    	if(opMedicalOffice.isPresent()) {
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    	//Lista di specializzazione controllata
    	List<Long> sup = new ArrayList <Long>();
    	for(int i=0; i<nomeSpecializzazione.size(); i++) {
    		for(String b: Specialization.fields) {
    			//Confronta per vedere se la specializzazione esiste
    			if(nomeSpecializzazione.get(i).equals(b)) {
    				sup.add(specializationRepository.findByField(nomeSpecializzazione.get(i)).get().getId());
    			}
    		}
    	}
    	//Se la specializzazione è vuota
    	if(sup.isEmpty()) {
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    	//Inizializza il nuovo dottore
    	Doctor doctor = new Doctor();
    	//riempimento
    	doctor.setUser(user);
    	doctor.setMedicalOffice(opMedicalOffice.get());
    	//salva l'utente
    	doctorRepository.save(doctor);
    	//Salva le relazione tra il dottore e le specializzazione
    	doctorSpecializationRepository.saveAllWithDoctorId(doctor.getId(), sup);
        return new ResponseEntity<>(doctor, HttpStatus.CREATED);
    }*/
    
    /**
     * Metodo di utilità per estrarre il token di autenticazione dall'header "Authorization".
     * Il token viene inviato nel formato "Bearer <token>".
     *
     * @param request Oggetto HttpServletRequest contenente gli header della richiesta.
     * @return L'oggetto User associato al token, oppure Optional.empty() se il token non è presente o non valido.
     */
    private Optional<User> getAuthenticatedUser(HttpServletRequest request) {
        // Legge l'header "Authorization"
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && !authHeader.isEmpty()) {
            String token;
            // Se il token è inviato come "Bearer <token>", lo estrae
            if (authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            } else {
                token = authHeader;
            }
            // Usa il TokenService per ottenere l'utente associato al token
            return tokenService.getAuthUser(token);
        }
        // Se non c'è header "Authorization", restituisce Optional.empty()
        System.out.println("Se non c'è header \"Authorization\", restituisce Optional.empty()");
        return Optional.empty();
    }

}
