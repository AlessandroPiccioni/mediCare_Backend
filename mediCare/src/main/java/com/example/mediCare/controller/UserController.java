package com.example.mediCare.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mediCare.auth.TokenService;
import com.example.mediCare.model.User;
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
    public Object verifiedUtenteUser(@Valid @RequestBody User user,HttpServletRequest request, HttpServletResponse response) {
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
    	//Gli rimanda l'user controlla cosi che possa continuare nella registrazione del medico
        return new ResponseEntity<>(user, HttpStatus.CONTINUE);
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
