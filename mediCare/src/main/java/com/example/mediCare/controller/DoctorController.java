package com.example.mediCare.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    @PostMapping 
    public Object createUtenteUser(@RequestBody User user, @PathVariable String codiceFiscale, @PathVariable List<String> nomeSpecializzazione,HttpServletRequest request, HttpServletResponse response) {
    	//Cerca se l'utente eiste
    	if(userRepository.findByEmailAndPasswordAndCodiceFiscale(user.getEmail(), user.getPassword(), user.getCodiceFiscale()).isPresent()) {
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    	Optional<MedicalOffice> opMedicalOffice =medicalOfficeRepository.findByCodiceFiscale(codiceFiscale);
    	//Controlla se lo studio medico ricercato esista
    	if(opMedicalOffice.isPresent()) {
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    	//Lista di specializzazione controllata
    	List<Specialization> sup = new ArrayList <Specialization>();
    	for(int i=0; i<nomeSpecializzazione.size(); i++) {
    		for(String b: Specialization.fields) {
    			//Confronta per vedere se la specializzazione esiste
    			if(nomeSpecializzazione.get(i).equals(b)) {
    				sup.add(specializationRepository.findByField(nomeSpecializzazione.get(i)).get());
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
    	moreDoctorSpecialization(doctor,sup);
        return new ResponseEntity<>(doctor, HttpStatus.CREATED);
    }
    
	//Crea una relazione con lo stesso id medico con tutte gli id specializzazione di una lista
	//Metodo per creare piu di una volta la relazione tra un solo medico con piu specializzazioni
    public void moreDoctorSpecialization(Doctor doctor, List<Specialization> specialization) {
        List<DoctorSpecialization> doctorSpecializations = new ArrayList<>();
        for (Specialization spec : specialization) {
            doctorSpecializations.add(new DoctorSpecialization(doctor, spec));
        }
        doctorSpecializationRepository.saveAll(doctorSpecializations);
    }
    
    /**
     * fa l'uploaddell'immagine per il prodotto
     * @param id
     * @param file
     * @param request
     * @param response
     * @return Object
     */
    @PostMapping("/image")
    public Object uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
        	//Oggetto Optional che rappresenta l'utente che ha fatto la richiesta
        	Optional<User> authUser = getAuthenticatedUser(request);
        	//Controlla se ha i permessi
        	if (!authUser.isPresent() && !authUser.get().getRuolo().name().equals("Medico")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return Collections.singletonMap("message", "Autenticazione richiesta");
            }
        	Optional<Doctor> OpDoctor = doctorRepository.findByUserId(authUser.get().getId());
        	if(!OpDoctor.isPresent()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return Collections.singletonMap("message", "Richiesta non valida");
        	}
        	Doctor doctor = OpDoctor.get();
        	doctor.setNomeFile(file.getOriginalFilename());
        	doctor.setData(file.getBytes());
        	Doctor savedDoctor = doctorRepository.save(doctor);
            // Restituisce una ResponseEntity con lo stato HTTP 201 (Created) e l'immagine salvata
            return new ResponseEntity<>(savedDoctor, HttpStatus.CREATED);
        } catch (IOException e) {
            // In caso di errore durante la lettura dei dati del file, restituisce lo stato HTTP 500 (Internal Server Error)
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
	 * elimina l'immagine del prodotto
	 * @param id
	 * @return ResponseEntity<byte[]>
	 */
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImageForProdotto (@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        try {
        	//Oggetto Optional che rappresenta l'utente che ha fatto la richiesta
        	Optional<User> authUser = getAuthenticatedUser(request);
        	if()
            Optional<Doctor> OpDoctor = doctorRepository.findById(id);
            if (!OpDoctor.isPresent() || OpDoctor.get().getNomeFile() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            Doctor doctor = OpDoctor.get();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doctor.getNomeFile() + "\"")
                    .body(doctor.getData());
        } catch (Exception e) {
            // In caso di errore durante la lettura dei dati del file, restituisce lo stato HTTP 500 (Internal Server Error)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    
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
