package com.example.mediCare.controller;

import com.example.mediCare.model.MedicalOffice;
import com.example.mediCare.repository.MedicalOfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/medical-offices")
@CrossOrigin(origins = "*") // Permette richieste da qualsiasi frontend
public class MedicalOfficeController {

    @Autowired
    private MedicalOfficeRepository repository;

    // Ottenere tutti gli studi medici
    @GetMapping
    public List<MedicalOffice> getAllMedicalOffices() {
        return repository.findAll();
    }

    // Ottenere uno studio medico per ID
    @GetMapping("/{id}")
    public ResponseEntity<MedicalOffice> getMedicalOfficeById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Cercare studi medici per nome
    @GetMapping("/search")
    public List<MedicalOffice> searchMedicalOffices(@RequestParam String nome) {
        return repository.findByNomeContainingIgnoreCase(nome);
    }

    // Creare un nuovo studio medico con immagine
    @PostMapping
    public ResponseEntity<MedicalOffice> createMedicalOffice(
            @RequestParam String nome,
            @RequestParam String specializzazione,
            @RequestParam(required = false) MultipartFile immagine) {

        if (nome == null || nome.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

     /*   try {
            // Crea una nuova istanza di MedicalOffice
            MedicalOffice office = new MedicalOffice();
            office.setNome(nome);
            office.setSpecializzazione(specializzazione);

            // Gestire l'immagine
            if (immagine != null && !immagine.isEmpty()) {
                office.setImmagine(immagine.getBytes()); // Converti l'immagine in byte[]
            }

            // Salva lo studio medico nel database
            MedicalOffice savedOffice = repository.save(office);
            return ResponseEntity.ok(savedOffice);

        } catch (IOException e) {
            e.printStackTrace();  // Aggiungi un log per vedere l'eccezione
            return ResponseEntity.status(500).body(null); // Errore nella gestione dell'immagine
        } catch (Exception e) {
            e.printStackTrace();  // Aggiungi un log per altre eccezioni
            return ResponseEntity.status(500).body(null);  // Gestione di altre eccezioni generali
        }*/
       // Crea una nuova istanza di MedicalOffice
        MedicalOffice office = new MedicalOffice();
        office.setNome(nome);
        office.setSpecializzazione(specializzazione);
        // Salva lo studio medico nel database
        MedicalOffice savedOffice = repository.save(office);
        return ResponseEntity.ok(savedOffice);

    }


    // Eliminare uno studio medico
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalOffice(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
