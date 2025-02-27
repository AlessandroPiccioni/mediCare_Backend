package com.example.mediCare.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.mediCare.auth.TokenService;
import com.example.mediCare.model.Doctor;
import com.example.mediCare.model.DoctorSpecialization;
import com.example.mediCare.model.Specialization;
import com.example.mediCare.model.User;
import com.example.mediCare.repository.DoctorRepository;
import com.example.mediCare.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
@Validated
//@CrossOrigin("*")
@CrossOrigin(origins = {})
//@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private DoctorRepository doctorRepository;
	
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
    public Object createUtenteUser(@Valid @RequestBody User user,HttpServletRequest request, HttpServletResponse response) {
    	//Controlla il ruolo dell'utente
    	//if(user.getRuolo().equals(User.Ruolo.Paziente) || user.getRuolo().equals(User.Ruolo.Medico)) {
    		//Richiesta fallita
    		//return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	//}
    	System.out.println("Sono entrato qui dentro");
    	user.setRuolo(User.Ruolo.Paziente);
    	//salva l'utente
    	User savedUtente = userRepository.save(user);
        return new ResponseEntity<>(savedUtente, HttpStatus.CREATED);
    }
    
    /**
     * Endpoint per la verifica dell'utente e per continuare la registrazione del medico
     * 
     * @param utente		Utente da registrare
     * @param request		Oggetto HttpServletRequest che contiene informazioni della richietsa
     * @param response		Oggetto HttpServletResponse che contiene informazioni della risposta 
     * @return ritorna la risposta dell'endpint composta da httpstatus e dal nuovo oggetto User
     */
    @PostMapping("/verified")
    public User verifiedUtenteUser(@Valid @RequestBody User user,HttpServletRequest request, HttpServletResponse response) {
    	//Controlla il ruolo dell'utente
    	//if(user.getRuolo().equals(User.Ruolo.Paziente) || user.getRuolo().equals(User.Ruolo.Medico)) {
    		//Richiesta fallita
    		//return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	//}
    	System.out.println("Sono entrato qui dentro");
    	if(user == null) {
    		System.out.println("è vuoro");
    	}
    	user.setRuolo(User.Ruolo.Medico);
    	System.out.println("prima del return");
    	//Gli rimanda l'user controlla cosi che possa continuare nella registrazione del medico
        //return new ResponseEntity<>(user, HttpStatus.CONTINUE);
    	return user;
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
    
    @GetMapping("/details")
    public ResponseEntity<Object> getDetails(@RequestHeader("Authorization") String authHeader)
    {
        Map<String, Object> result = new HashMap<>();
        if (authHeader == null || authHeader.isEmpty())
        {
            result.put("errore", "Nessun token fornito");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }

        String token;
        if (authHeader != null && authHeader.startsWith("Bearer ")) 
        {
            token = authHeader.substring(7);
        } else
        {
            token = authHeader; 
        }
        
        if (token == null || token.isEmpty())
        {
            result.put("errore", "Token non valido");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
        
        Optional<User> authUserOpt = userRepository.findByToken(token);
        if (!authUserOpt.isPresent())
        {
            result.put("errore", "Errore token non valido");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
        
        User authUser = authUserOpt.get();
        String email = authUser.getEmail();
        Optional<User> utenteOpt = userRepository.findByEmail(email);
        if (!utenteOpt.isPresent())
        {
            result.put("errore", "Non esiste utente con tale email.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        
        User utente = utenteOpt.get();

        // Dati utente
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", utente.getId());
        userMap.put("nome", utente.getNome());
        userMap.put("cognome", utente.getCognome());
        userMap.put("codiceFiscale", utente.getCodiceFiscale());
        userMap.put("email", utente.getEmail());
        userMap.put("dataNascita", utente.getDataNascita() != null ? 
                                   utente.getDataNascita().toString() : null);
        userMap.put("ruolo", utente.getRuolo() != null ? 
                             utente.getRuolo().toString() : null);
        
        Map<String, Object> response = new HashMap<>();
        response.put("user", userMap);
        
        // Cerca il dottore associato all'utente
        Optional<Doctor> doctorOpt = doctorRepository.findByUserId(utente.getId());
        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            
            Map<String, Object> doctorMap = new HashMap<>();
            doctorMap.put("id", doctor.getId());
            
            // Gestione delle specializzazioni
            if (doctor.getDoctorSpecialization() != null && !doctor.getDoctorSpecialization().isEmpty()) {
                List<Map<String, Object>> specializationList = new ArrayList<>();
                
                for (DoctorSpecialization docSpec : doctor.getDoctorSpecialization()) {
                    Map<String, Object> specMap = new HashMap<>();
                    specMap.put("id", docSpec.getId());
                    
                    // getSpecialization() che restituisce l'entità Specialization
                    if (docSpec.getSpecialization() != null) {
                        specMap.put("specializationId", docSpec.getSpecialization().getId());
                        specMap.put("nome", docSpec.getSpecialization().getField());
                       
                    }
                    
                    specializationList.add(specMap);
                }
                
                doctorMap.put("specializzazioni", specializationList);
            }
            
            // Informazioni dell'ufficio medico
            if (doctor.getMedicalOffice() != null) {
                Map<String, Object> officeMap = new HashMap<>();
                officeMap.put("id", doctor.getMedicalOffice().getId());
                officeMap.put("nome", doctor.getMedicalOffice().getNome());
                
                
                doctorMap.put("studioMedico", officeMap);
            }
            
            response.put("doctor", doctorMap);
        }
        
        return ResponseEntity.ok(response);
    }
}
