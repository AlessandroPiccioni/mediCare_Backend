package com.example.mediCare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mediCare.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	//Cerca l'utente tramite il token
	Optional<User> findByToken(String token);
	
	//Cerca l'utente tramite email
	Optional<User> findByEmail(String email);
	
	//Cerca tramite password
	Optional<User> findByPassword(String password);
	
	//Cerca l'utente tramite l'email, la password e il codice fiscale
	Optional<User> findByEmailAndPasswordAndCodiceFiscale(String email, String password, String codiceFiscale);

}
