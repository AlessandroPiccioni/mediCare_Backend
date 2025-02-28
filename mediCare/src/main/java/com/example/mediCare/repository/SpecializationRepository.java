package com.example.mediCare.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mediCare.model.Specialization;
import com.example.mediCare.model.User;

@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, Long> {
	
	Optional<Specialization> findByField(String field);
	
	List<Specialization> findByFieldIn(List<String> fields);

}
