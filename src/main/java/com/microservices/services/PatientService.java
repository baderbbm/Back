package com.microservices.services;

import com.microservices.domain.Genre;
import com.microservices.domain.Patient;
import com.microservices.exceptions.PatientNotFoundException;
import com.microservices.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

	@Autowired
	private PatientRepository patientRepository;

	public List<Patient> getAllPatients() {
		return patientRepository.findAll();
	}

	public Optional<Patient> getPatientById(Long id) {
		return patientRepository.findById(id);
	}

	public Patient updatePatientAdresse(Long id, String nouvelleAdresse) {
		Optional<Patient> optionalPatient = patientRepository.findById(id);
		if (optionalPatient.isPresent()) {
			Patient patient = optionalPatient.get();
			patient.setAdressePostale(nouvelleAdresse);
			return patientRepository.save(patient);
		} else {
			throw new PatientNotFoundException("Patient non trouvé avec l'ID : " + id);
		}
	}

	public Patient updatePatientNumero(Long id, String nouveauNumero) {
		Optional<Patient> optionalPatient = patientRepository.findById(id);
		if (optionalPatient.isPresent()) {
			Patient patient = optionalPatient.get();
			patient.setNumeroTelephone(nouveauNumero);
			return patientRepository.save(patient);
		} else {
			throw new PatientNotFoundException("Patient non trouvé avec l'ID : " + id);
		}
	}

	public Patient addNewPatient(String prenom, String nom, java.sql.Date dateNaissance, Genre genre,
			String adressePostale, String numeroTelephone) {
		Patient newPatient = new Patient();
		newPatient.setPrenom(prenom);
		newPatient.setNom(nom);
		newPatient.setDateNaissance(dateNaissance);
		newPatient.setGenre(genre);
		newPatient.setAdressePostale(adressePostale);
		newPatient.setNumeroTelephone(numeroTelephone);

		return patientRepository.save(newPatient);
	}
}
