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
import org.springframework.web.bind.annotation.DeleteMapping;
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
    @PostMapping ("/{codiceFiscale}/{nomeSpecializzazione}")
    public Object createUtenteDoctor(@RequestBody User user, @PathVariable String codiceFiscale, @PathVariable List<Long> nomeSpecializzazione,HttpServletRequest request, HttpServletResponse response) {
    	
    	System.out.println("Sei entrato nel endpoint per creare il nuovo dottore");
    	
    	//Controllare se lo studio medico esista
    	Optional<MedicalOffice> opMedicalOffice= medicalOfficeRepository.findByCodiceFiscale(codiceFiscale);
    	if(!opMedicalOffice.isPresent()) {
    		System.out.println("Lo studio medico non è presente nel database");
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    	
    	
    	//Controllare se le specializzazioni esistano
    	
    	List<Long> sup = new ArrayList <Long>();
    	for(int i=0; i<nomeSpecializzazione.size(); i++) {
    		if(nomeSpecializzazione.get(i)!= null && specializationRepository.findById(nomeSpecializzazione.get(i)).isPresent()) {
    			System.out.println("Specializzazione controllata: " + nomeSpecializzazione.get(i));
    			sup.add(nomeSpecializzazione.get(i));
    		}
    	}
    	
    	if(sup.isEmpty()) {
    		System.out.println("Nessuna specializzazione usata eiste");
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    	
    	//Creare il nuvo user
    	
    	user.setRuolo(User.Ruolo.Medico);
    	userRepository.save(user);
    	
    	System.out.println("Creazione User medico: " + user);
    	
    	//Controllare se l'user esista
    	System.out.println("Prima del controllo user");
    	Optional<User> OpSaveUser = userRepository.findByEmail(user.getEmail());
    	System.out.println();
    	System.out.println(OpSaveUser.get().getId());
    	System.out.println(!OpSaveUser.isPresent());
    	if (!OpSaveUser.isPresent()) {
    	    System.out.println("L'utente con questa email non esiste");
    	    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    	
    	System.out.println("DOPO del controllo user");
    	
    	//Inserire il medico insieme allo studio medico e lo user
    	
    	// Creare il nuovo dottore
    	Doctor doctor = new Doctor();

    	// Aggiungiamo l'user al dottore
    	System.out.println("Aggiungiamo l'user al dottore");
    	doctor.setUser(OpSaveUser.get());

    	// Aggiungiamo lo studio medico al dottore
    	System.out.println("Aggiungiamo lo studio medico al dottore");
    	doctor.setMedicalOffice(opMedicalOffice.get());

    	// Salviamo il dottore nel database prima di fare la ricerca
    	doctorRepository.save(doctor); // Salvataggio nel database

    	// Verifica che il dottore è stato correttamente salvato
    	System.out.println("Verifichiamo se il dottore è stato salvato nel database");
    	Optional<Doctor> opSaveDoctor = doctorRepository.findByUserId(OpSaveUser.get().getId());

    	if (!opSaveDoctor.isPresent()) {
    	    System.out.println("Il dottore non è stato salvato correttamente");
    	    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}

    	System.out.println("Il dottore è stato salvato correttamente");
    	
    	//inserire le relazioni tra il singolo dottore e le specializzazioni
    	
    	Doctor saveDoctor= opSaveDoctor.get();
    	List<DoctorSpecialization> doctorSpecializations = new ArrayList<>();
    	for(int i=0; i<sup.size(); i++) {
    		Optional <Specialization> Opspecialization = specializationRepository.findById(sup.get(i));
    		doctorSpecializations.add(new DoctorSpecialization(saveDoctor, Opspecialization.get()));
    	}
    	
    	System.out.println("Le specializzazioni stannto per essere associate");
    	doctorSpecializationRepository.saveAll(doctorSpecializations);
    	
    	return new ResponseEntity<>(saveDoctor, HttpStatus.CREATED);
   
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
    public Object uploadImage(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
        	
        	//Controllo del file
        	if(file.isEmpty()) {
        		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return Collections.singletonMap("message", "Nessun file caricato");
        	}
            if (file.getSize() > Doctor.Max_file_size) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return Collections.singletonMap("message", "Il file è troppo grande. La dimensione massima consentita è " + Doctor.Max_file_size + " byte.");
            }
            //Controlla i mime
            if (!Doctor.Content_Types.contains(file.getContentType())) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return Collections.singletonMap("message", "Tipo di file non supportato. I tipi supportati sono: " + String.join(", ", Doctor.Content_Types));
            }
             	
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
    

    @GetMapping("/image/{id}")
    public Object getImageForProdotto (HttpServletRequest request, HttpServletResponse response) {
        try {
        	//Oggetto Optional che rappresenta l'utente che ha fatto la richiesta
        	Optional<User> authUser = getAuthenticatedUser(request);
           	//Controlla se ha i permessi
        	if (!authUser.isPresent() && !authUser.get().getRuolo().name().equals("Medico")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return Collections.singletonMap("message", "Autenticazione richiesta");
            }
            Optional<Doctor> OpDoctor = doctorRepository.findByUserId(authUser.get().getId());
            if (!OpDoctor.isPresent() || OpDoctor.get().getNomeFile() == null) {
            	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
	 * ottiene tutte le immagini
	 * @return List<ResponseEntity<byte[]>>
	 */
	@GetMapping("/image/all")
	public List<ResponseEntity<byte[]>> getAllImages() {
	    List<ResponseEntity<byte[]>> responses = new ArrayList<>();
	    for (Doctor doctor : doctorRepository.findAll()) {
	        if (doctor.getNomeFile() != null) {
	            ResponseEntity<byte[]> response = ResponseEntity.ok()
	            	.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doctor.getNomeFile() + "\"")
	                .body(doctor.getData());
	            responses.add(response);  
	        } else {
	            ResponseEntity<byte[]> response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	            responses.add(response);  
	        }
	    }
	    return responses;
	}
	
    /**
     * elimina un immagine dato un id
     * @param id
     * @param request
     * @param response
     * @return Object
     */
    @DeleteMapping("/image/{id}")
    public Object deleteImage(HttpServletRequest request, HttpServletResponse response) {
    	Optional<User> authUser = getAuthenticatedUser(request);
    	//Controlla se ha i permessi
    	if (!authUser.isPresent() && !authUser.get().getRuolo().equals("admin")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return Collections.singletonMap("message", "Autenticazione richiesta");
        }
    	Optional<Doctor> OpDoctor = doctorRepository.findByUserId(authUser.get().getId());
        if(!OpDoctor.isPresent()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return Collections.singletonMap("message", "Richiesta non valida");
        }
        Doctor doctor = OpDoctor.get();
        doctor.setNomeFile(null);
        doctor.setData(null);
        Doctor deleteDoctor = doctorRepository.save(doctor);
        
        //Ritorna la risposta dell'endpoint
        return new ResponseEntity<>(deleteDoctor, HttpStatus.OK);
        
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
