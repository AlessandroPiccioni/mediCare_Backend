package com.example.mediCare.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.mediCare.model.ListServices;
import com.example.mediCare.model.MedicalOffice;
import com.example.mediCare.repository.ListServicesRepository;
import com.example.mediCare.repository.MedicalOfficeRepository;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "*") 
public class ListServicesController {

    @Autowired
    private ListServicesRepository listServicesRepository;  // Repository per la gestione delle prestazioni

    @Autowired
    private MedicalOfficeRepository medicalOfficeRepository;  // Repository per la gestione degli studi medici

    // Metodo per creare un servizio (prestazione)
    @PostMapping
    public ListServices createService(@RequestBody ListServices service) {
        // Troviamo lo studio medico in base all'ID
        Optional<MedicalOffice> optionalMedicalOffice = medicalOfficeRepository.findById(service.getMedicalOffice().getId());

        if (!optionalMedicalOffice.isPresent()) {
            throw new RuntimeException("Studio medico non trovato con ID: " + service.getMedicalOffice().getId());
        }

        // Impostiamo lo studio medico nel servizio
        MedicalOffice medicalOffice = optionalMedicalOffice.get();
        service.setMedicalOffice(medicalOffice);

        // Salviamo il servizio nel database
        try {
            return listServicesRepository.save(service); // Usa save() per una singola entità
        } catch (Exception e) {
            throw new RuntimeException("Errore durante il salvataggio del servizio: " + e.getMessage());
        }
    }

    // Metodo per ottenere tutti i servizi
    @GetMapping
    public List<ListServices> getAllServices() {
        return listServicesRepository.findAll();
    }

    // Metodo per ottenere un singolo servizio per ID
    @GetMapping("/{id}")
    public ListServices getServiceById(@PathVariable Long id) {
        Optional<ListServices> service = listServicesRepository.findById(id);
        if (service.isPresent()) {
            return service.get();
        } else {
            throw new RuntimeException("Servizio non trovato con ID: " + id);
        }
    }

    // Metodo per aggiornare un servizio esistente
    @PutMapping("/{id}")
    public ListServices updateService(@PathVariable Long id, @RequestBody ListServices updatedService) {
        Optional<ListServices> existingServiceOpt = listServicesRepository.findById(id);
        
        if (!existingServiceOpt.isPresent()) {
            throw new RuntimeException("Servizio non trovato con ID: " + id);
        }

        // Aggiungiamo la logica per aggiornare i campi
        ListServices existingService = existingServiceOpt.get();
        existingService.setNome(updatedService.getNome());
        existingService.setDescrizione(updatedService.getDescrizione());
        existingService.setMedico(updatedService.getMedico());
        existingService.setDisponibilita(updatedService.getDisponibilita());

        // Se lo studio medico è cambiato, lo aggiorniamo
        if (updatedService.getMedicalOffice() != null) {
            Optional<MedicalOffice> optionalMedicalOffice = medicalOfficeRepository.findById(updatedService.getMedicalOffice().getId());
            if (optionalMedicalOffice.isPresent()) {
                existingService.setMedicalOffice(optionalMedicalOffice.get());
            } else {
                throw new RuntimeException("Studio medico non trovato con ID: " + updatedService.getMedicalOffice().getId());
            }
        }

        // Salviamo il servizio aggiornato nel database
        return listServicesRepository.save(existingService);
    }

    // Metodo per eliminare un servizio
    @DeleteMapping("/{id}")
    public String deleteService(@PathVariable Long id) {
        Optional<ListServices> service = listServicesRepository.findById(id);

        if (!service.isPresent()) {
            throw new RuntimeException("Servizio non trovato con ID: " + id);
        }

        listServicesRepository.deleteById(id);
        return "Servizio con ID " + id + " è stato eliminato";
    }
}
