package com.example.mediCare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mediCare.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByToken(String token);
	
	Optional<User> findByEmail(String email);
	
	Optional<User> findByPassword(String password);

}
