package com.microservices;

import com.microservices.domain.Genre;
import com.microservices.domain.Patient;
import com.microservices.exceptions.PatientNotFoundException;
import com.microservices.repositories.PatientRepository;
import com.microservices.services.PatientService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    @Test
    public void testGetAllPatients() {
        // Créez une liste avec 5 patients simulés
        List<Patient> patients = Arrays.asList(
                new Patient(), new Patient(), new Patient(), new Patient(), new Patient());

        // Simulez le comportement du repository pour retourner cette liste
        when(patientRepository.findAll()).thenReturn(patients);

        // Appelez la méthode dans le service
        List<Patient> result = patientService.getAllPatients();

        // Vérifiez si la taille de la liste est égale à 5
        assertNotNull(result);
        assertEquals(5, result.size());
    }


    @Test
    public void testGetPatientById() {
        Long patientId = 1L;
        Patient patient = new Patient();
        patient.setId(patientId);
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        Optional<Patient> result = patientService.getPatientById(patientId);

        assertTrue(result.isPresent());
        assertEquals(patientId, result.get().getId());
    }

    @Test
    public void testUpdatePatientAdresse() {
        Long patientId = 1L;
        String nouvelleAdresse = "Nouvelle Adresse";
        Patient patient = new Patient();
        patient.setId(patientId);
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(patientRepository.save(Mockito.any(Patient.class))).thenReturn(patient);

        Patient result = patientService.updatePatientAdresse(patientId, nouvelleAdresse);

        assertNotNull(result);
        assertEquals(nouvelleAdresse, result.getAdressePostale());
    }

    @Test
    public void testUpdatePatientAdresseNotFound() {
        Long patientId = 1L;
        String nouvelleAdresse = "Nouvelle Adresse";
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> patientService.updatePatientAdresse(patientId, nouvelleAdresse));
    }
    
    @Test
    public void testUpdatePatientNumero() {
        // Créer un objet Patient pour les tests
        Patient testPatient = new Patient();
        testPatient.setId(1L);
        testPatient.setNumeroTelephone("123456789");

        // Configurer le comportement du mock repository
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(patientRepository.save(Mockito.any(Patient.class))).thenReturn(testPatient);

        // Appeler la méthode de service à tester
        Patient updatedPatient = patientService.updatePatientNumero(1L, "987654321");

        // Vérifier que la méthode de repository a été appelée avec les bons arguments
        Mockito.verify(patientRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(patientRepository, Mockito.times(1)).save(testPatient);

        // Vérifier que les modifications ont été apportées correctement
        assertEquals("987654321", updatedPatient.getNumeroTelephone());
    }

    @Test
    public void testAddNewPatient() {
        // Créer des données de test pour la nouvelle méthode
        String prenom = "John";
        String nom = "Doe";
        Date dateNaissance = Date.valueOf("1990-01-01");
        Genre genre = Genre.M;
        String adressePostale = "123 Main St";
        String numeroTelephone = "555-1234";

        // Créer un objet Patient pour les tests
        Patient expectedPatient = new Patient();
        expectedPatient.setPrenom(prenom);
        expectedPatient.setNom(nom);
        expectedPatient.setDateNaissance(dateNaissance);
        expectedPatient.setGenre(genre);
        expectedPatient.setAdressePostale(adressePostale);
        expectedPatient.setNumeroTelephone(numeroTelephone);

        // Configurer le comportement du mock repository
        when(patientRepository.save(Mockito.any(Patient.class))).thenReturn(expectedPatient);

        // Appeler la méthode de service à tester
        Patient newPatient = patientService.addNewPatient(prenom, nom, dateNaissance, genre, adressePostale, numeroTelephone);

        // Vérifier que la méthode de repository a été appelée avec les bons arguments
        Mockito.verify(patientRepository, Mockito.times(1)).save(Mockito.any(Patient.class));

        // Vérifier que le patient a été créé correctement
        assertNotNull(newPatient);
        assertEquals(expectedPatient.getPrenom(), newPatient.getPrenom());
        assertEquals(expectedPatient.getNom(), newPatient.getNom());
        assertEquals(expectedPatient.getDateNaissance(), newPatient.getDateNaissance());
        assertEquals(expectedPatient.getGenre(), newPatient.getGenre());
        assertEquals(expectedPatient.getAdressePostale(), newPatient.getAdressePostale());
        assertEquals(expectedPatient.getNumeroTelephone(), newPatient.getNumeroTelephone());
    }
    
    @Test
    public void testUpdatePatientNumeroPatientNotFound() {
        // ID du patient inexistant
        Long id = 10L;

        // Configurer le mock du repository pour renvoyer Optional vide
        when(patientRepository.findById(id)).thenReturn(Optional.empty());

        // Appeler la méthode de service à tester et vérifier l'exception
        assertThrows(PatientNotFoundException.class, () -> {
            patientService.updatePatientNumero(id, "nouveauNumero");
        });

        // Vérifier que le repository a été appelé avec le bon ID
        verify(patientRepository, times(1)).findById(id);

        // Vérifier qu'aucun enregistrement n'a été effectué
        verify(patientRepository, never()).save(any());
    }
}
