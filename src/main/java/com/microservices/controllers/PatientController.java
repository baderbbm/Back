package com.microservices.controllers;

import com.microservices.domain.Patient;
import com.microservices.exceptions.PatientNotFoundException;
import com.microservices.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping("/all")
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long patientId) {
        try {
            Optional<Patient> patient = patientService.getPatientById(patientId);
            return patient.map(ResponseEntity::ok)
                          .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (PatientNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{patientId}/update-adresse")
    public ResponseEntity<Patient> updatePatientAdresse(
            @PathVariable Long patientId,
            @RequestParam String nouvelleAdresse) {

        try {
            Patient patient = patientService.updatePatientAdresse(patientId, nouvelleAdresse);
            return ResponseEntity.ok(patient);
        } catch (PatientNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{patientId}/update-numero")
    public ResponseEntity<Patient> updatePatientNumero(
            @PathVariable Long patientId,
            @RequestParam String nouveauNumero) {

        try {
            Patient patient = patientService.updatePatientNumero(patientId, nouveauNumero);
            return ResponseEntity.ok(patient);
        } catch (PatientNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
