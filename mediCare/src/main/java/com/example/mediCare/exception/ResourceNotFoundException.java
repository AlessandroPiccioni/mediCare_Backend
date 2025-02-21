package com.example.mediCare.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//Restituisce http status 404
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
	
  // Costruttore 
  public ResourceNotFoundException(String message) {
      //Chiama il costruttore della superclasse RuntimeException
      super(message);
  }

}