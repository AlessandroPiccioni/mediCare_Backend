package com.example.mediCare;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.mediCare.model.Doctor;
import com.example.mediCare.model.DoctorSpecialization;
import com.example.mediCare.model.MedicalOffice;
import com.example.mediCare.model.Specialization;
import com.example.mediCare.model.User;
import com.example.mediCare.model.User.Ruolo;
import com.example.mediCare.repository.DoctorRepository;
import com.example.mediCare.repository.DoctorSpecializationRepository;
import com.example.mediCare.repository.MedicalOfficeRepository;
import com.example.mediCare.repository.SpecializationRepository;
import com.example.mediCare.repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private MedicalOfficeRepository medicalOfficeRepository;
    
    @Autowired
    private SpecializationRepository specializationRepository;
    
    @Autowired
    private DoctorSpecializationRepository doctorSpecializationRepository;
    
    /**
     * Metodo eseguito al termine dell'avvio dell'applicazione. 
     * Se il database è vuoto, inserisce utenti, dottori, specializzazioni e studi medici di esempio.
     */
    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0 && medicalOfficeRepository.count() == 0) {
            // Creiamo prima le specializzazioni
            List<Specialization> specializzazioni = creaSpecializzazioni();
            
            // Creiamo gli studi medici
            List<MedicalOffice> studiMedici = creaStudiMedici();
            
            // Creiamo l'admin
            User admin = new User();
            admin.setNome("Admin");
            admin.setCognome("Sistema");
            admin.setCodiceFiscale("ADMNSST80A01H501");
            admin.setEmail("admin@medicare.it");
            admin.setPassword("amministratore123");
            admin.setDataNascita(LocalDate.of(1980, 1, 1));
            admin.setRuolo(Ruolo.Admin);
            userRepository.save(admin);
            
            // Creiamo utenti pazienti
            List<User> pazienti = creaPazienti();
            
            // Creiamo utenti medici con relative specializzazioni
            creaMedici(studiMedici, specializzazioni);
        }
    }
    
    private List<Specialization> creaSpecializzazioni() {
        List<Specialization> specializzazioni = new ArrayList<>();
        
        String[] campiSpecializzazione = {
            "Cardiologia", "Neurologia", "Pediatria", 
            "Ortopedia", "Dermatologia", "Oftalmologia",
            "Ginecologia", "Urologia", "Psichiatria"
        };
        
        for (String campo : campiSpecializzazione) {
            Specialization spec = new Specialization();
            spec.setField(campo);
            specializationRepository.save(spec);
            specializzazioni.add(spec);
        }
        
        return specializzazioni;
    }
    
    private List<MedicalOffice> creaStudiMedici() {
        List<MedicalOffice> studiMedici = new ArrayList<>();
        
        String[][] datiStudi = {
            {"Centro Medico Salute", "CNTMDC80A01H501X"},
            {"Poliambulatorio Benessere", "PLMBNR75A01H501Y"},
            {"Studio Medico Vita", "STDMDC70A01H501Z"}
        };
        
        for (String[] studio : datiStudi) {
            MedicalOffice medicalOffice = new MedicalOffice();
            medicalOffice.setNome(studio[0]);
            medicalOffice.setCodiceFiscale(studio[1]);
            medicalOfficeRepository.save(medicalOffice);
            studiMedici.add(medicalOffice);
        }
        
        return studiMedici;
    }
    
    private List<User> creaPazienti() {
        List<User> pazienti = new ArrayList<>();
        
        String[][] datiPazienti = {
            {"Mario", "Rossi", "MRRRSS80A01H501A", "mario.rossi@esempio.it", "1980-05-15"},
            {"Laura", "Bianchi", "LRABNC85A41H501B", "laura.bianchi@esempio.it", "1985-04-10"},
            {"Giuseppe", "Verdi", "GPPVRD75A01H501C", "giuseppe.verdi@esempio.it", "1975-10-20"},
            {"Francesca", "Neri", "FRNNRI90A41H501D", "francesca.neri@esempio.it", "1990-07-25"},
            {"Antonio", "Gialli", "NTNGLL82A01H501E", "antonio.gialli@esempio.it", "1982-03-12"},
            {"Sofia", "Blu", "SFABLU88A41H501F", "sofia.blu@esempio.it", "1988-11-05"}
        };
        
        for (String[] paziente : datiPazienti) {
            User user = new User();
            user.setNome(paziente[0]);
            user.setCognome(paziente[1]);
            user.setCodiceFiscale(paziente[2]);
            user.setEmail(paziente[3]);
            user.setPassword("password12345");
            user.setDataNascita(LocalDate.parse(paziente[4]));
            user.setRuolo(Ruolo.Paziente);
            userRepository.save(user);
            pazienti.add(user);
        }
        
        return pazienti;
    }
    
    private void creaMedici(List<MedicalOffice> studiMedici, List<Specialization> specializzazioni) {
        String[][] datiMedici = {
            {"Paolo", "Ferrari", "PLFRR75A01H501GI", "paolo.ferrari@esempio.it", "1975-06-18"},
            {"Elena", "Martini", "ELNMRT80A41H501H", "elena.martini@esempio.it", "1980-09-22"},
            {"Roberto", "Ricci", "RBTRCC72A01H501I", "roberto.ricci@esempio.it", "1972-02-14"},
            {"Claudia", "Moretti", "CLDMRT84A41H501J", "claudia.moretti@esempio.it", "1984-12-30"},
            {"Marco", "Esposito", "MRCEPS78A01H501K", "marco.esposito@esempio.it", "1978-08-07"},
            {"Chiara", "Romano", "CHRRMN83A41H501L", "chiara.romano@esempio.it", "1983-01-15"}
        };
        
        for (int i = 0; i < datiMedici.length; i++) {
            // Crea utente medico
            User userMedico = new User();
            userMedico.setNome(datiMedici[i][0]);
            userMedico.setCognome(datiMedici[i][1]);
            userMedico.setCodiceFiscale(datiMedici[i][2]);
            userMedico.setEmail(datiMedici[i][3]);
            userMedico.setPassword("dottore12345");
            userMedico.setDataNascita(LocalDate.parse(datiMedici[i][4]));
            userMedico.setRuolo(Ruolo.Medico);
            userRepository.save(userMedico);
            
            // Crea dottore e associalo all'utente e allo studio medico
            Doctor doctor = new Doctor();
            doctor.setUser(userMedico);
            // Assegna uno studio medico (distribuiti uniformemente)
            doctor.setMedicalOffice(studiMedici.get(i % studiMedici.size()));
            doctorRepository.save(doctor);
            
            // Associa una specializzazione al dottore
            DoctorSpecialization doctorSpec = new DoctorSpecialization();
            doctorSpec.setDoctor(doctor);
            // Assegna una specializzazione (distribuiti uniformemente)
            doctorSpec.setSpecialization(specializzazioni.get(i % specializzazioni.size()));
            doctorSpecializationRepository.save(doctorSpec);
        }
    }
}
