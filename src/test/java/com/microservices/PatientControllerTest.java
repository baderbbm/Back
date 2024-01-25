package com.microservices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.controllers.PatientController;
import com.microservices.domain.Genre;
import com.microservices.domain.Patient;
import com.microservices.exceptions.PatientNotFoundException;
import com.microservices.services.PatientService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class PatientControllerTest {

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    public void testGetAllPatients() throws Exception {
        List<Patient> realPatients = patientService.getAllPatients();

        when(patientService.getAllPatients()).thenReturn(realPatients);

        ResultActions resultActions = mockMvc.perform(get("/patients/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }
    
  @Test
    public void testGetPatientById() throws Exception {
        Long patientId = 1L; 
        Patient patient = new Patient(); 
        patient.setPrenom("Test");
        patient.setId(patientId);
        when(patientService.getPatientById(patientId)).thenReturn(Optional.of(patient));

        mockMvc.perform(MockMvcRequestBuilders.get("/patients/{patientId}", patientId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(patient.getId())) 
                .andExpect(MockMvcResultMatchers.jsonPath("$.prenom").value(patient.getPrenom()))
                .andReturn();
    }

    @Test
    public void testUpdatePatientAdresse() throws Exception {
        Long patientId = 1L; 
        String nouvelleAdresse = "Nouvelle adresse";

        when(patientService.updatePatientAdresse(patientId, nouvelleAdresse)).thenReturn(new Patient());

        mockMvc.perform(MockMvcRequestBuilders.put("/patients/{patientId}/update-adresse", patientId)
                .param("nouvelleAdresse", nouvelleAdresse))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void testUpdatePatientNumero() throws Exception {
        Long patientId = 1L;
        String nouveauNumero = "Nouveau numéro";

        when(patientService.updatePatientNumero(patientId, nouveauNumero)).thenReturn(new Patient());

        mockMvc.perform(MockMvcRequestBuilders.put("/patients/{patientId}/update-numero", patientId)
                .param("nouveauNumero", nouveauNumero))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }
    
    @Test
    public void testAddNewPatient() throws Exception {
        // Créez un nouvel objet Patient pour le corps de la requête
        Patient newPatient = new Patient();
        newPatient.setPrenom("John");
        newPatient.setNom("Doe");
        newPatient.setDateNaissance(Date.valueOf("1990-01-01"));
        newPatient.setGenre(Genre.M);
        newPatient.setAdressePostale("123 Main St");
        newPatient.setNumeroTelephone("555-1234");

        // Configurer le service pour renvoyer le patient ajouté
        when(patientService.addNewPatient(any(), any(), any(), any(), any(), any()))
                .thenReturn(newPatient);

        mockMvc.perform(post("/patients/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPatient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prenom", is("John")))
                .andExpect(jsonPath("$.nom", is("Doe")))
                .andExpect(jsonPath("$.dateNaissance", is("1990-01-01")))
                .andExpect(jsonPath("$.genre", is("M")))
                .andExpect(jsonPath("$.adressePostale", is("123 Main St")))
                .andExpect(jsonPath("$.numeroTelephone", is("555-1234")));
    }
    
    @Test
    public void testUpdatePatientAdressePatientNotFoundException() throws Exception {
        Long patientId = 100L;
        String nouvelleAdresse = "Nouvelle adresse";
        
        when(patientService.updatePatientAdresse(patientId, nouvelleAdresse))
                .thenThrow(new PatientNotFoundException("Patient non trouvé avec l'ID : " + patientId));

        mockMvc.perform(put("/patients/{patientId}/update-adresse", patientId)
                .param("nouvelleAdresse", nouvelleAdresse))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void testUpdatePatientNumeroPatientNotFoundException() throws Exception {
        Long patientId = 100L;
        String nouveauNumero = "123456789";

        when(patientService.updatePatientNumero(patientId, nouveauNumero))
                .thenThrow(new PatientNotFoundException("Patient non trouvé avec l'ID : " + patientId));

        mockMvc.perform(put("/patients/{patientId}/update-numero", patientId)
                .param("nouveauNumero", nouveauNumero))
                .andExpect(status().isNotFound());
    }
   
    @Test
    public void testGetPatientByIdPatientNotFoundException() throws Exception {
        Long patientId = 100L;

        when(patientService.getPatientById(patientId)).thenThrow(new PatientNotFoundException("Patient non trouvé avec l'ID : " + patientId));

        mockMvc.perform(get("/patients/{patientId}", patientId))
                .andExpect(status().isNotFound());
    }
}
