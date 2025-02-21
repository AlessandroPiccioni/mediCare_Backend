package com.example.mediCare.auth;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mediCare.model.User;
import com.example.mediCare.repository.UserRepository;

/**
 * Servizio che si occupa della generazione e della gestione dei token di autenticazione.
 * I token vengono memorizzati in una mappa in memoria, associando ad ogni token un oggetto AuthUser.
 */
@Service
public class TokenService {
	
	@Autowired
    private UserRepository userRepository;

    /**
     * Metodo che genera un token casuale (UUID) e lo associa ad un utente autenticato.
     *
     * @param email		l'email dell'utente
     * @param role		il ruolo dell'utente (es. admin o user)
     * @return il token generato
     */
    public String generateToken(String email, String password) {
    	
    	//Controlla se i campi del login siano giusti
       Optional<User> optionalUser = userRepository.findByEmail(email);
        
        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("L'utente non esiste");
        }
        
        User user = optionalUser.get();
        
        if(!user.getEmail().equals(email)&& !user.getPassword().equals(password)) {
        	 throw new IllegalArgumentException("Credenziali non valide");
        }
    	
    	 // Genera un token univoco usando UUID
        String token = UUID.randomUUID().toString();
      
        System.out.println("Token generato: " + token); //stringa di debug in console
        
        user.setToken(token);

        //Salva l'utente con il nuovo token
        userRepository.save(user);

        return token;
    }

    /**
     * Restituisce l'oggetto Utente associato al token.
     *
     * @param token il token di autenticazione
     * @return l'oggetto AuthUser, oppure null se il token non è valido
     */
    public Optional<User> getAuthUser(String token) {
      	Optional<User> optionalUser = userRepository.findByToken(token);
      	if(!optionalUser.isPresent()) {
      		System.out.println("non è presente");
      	}
        return optionalUser;
    }

    /**
     * Rimuove un token dalla mappa, ad esempio durante il logout.
     *
     * @param token il token da rimuovere
     */
    public void removeToken(String token) {
    	Optional<User> optionalUser = userRepository.findByToken(token);
    	if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("Token non trovato: " + token);
        }
        User user = optionalUser.get();  
        user.setToken(null);
        userRepository.save(user);
    }
}
