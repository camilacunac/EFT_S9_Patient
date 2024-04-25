package com.example.patients.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.patients.dto.AttentionDTO;
import com.example.patients.dto.ConsultationDTO;
import com.example.patients.dto.UpdateDirectionDTO;
import com.example.patients.model.Attention;
import com.example.patients.model.Consultation;
import com.example.patients.model.Patient;
import com.example.patients.model.Response;

public interface PatientService {

    List<Patient> getAllPatients();

    ResponseEntity<Response> getPatientById(Long id);

    List<Consultation> getAllConsultationsByPatientId(Long id);

    List<Attention> getAllAttentionsByPatientId(Long id);

    ResponseEntity<Response> addPatient(Patient patient);

    ResponseEntity<Response> addConsultForPatient(Long idPatient, ConsultationDTO data);

    ResponseEntity<Response> addAttentionForPatient(long idPatient, AttentionDTO data);

    ResponseEntity<Response> updatePatientDirection(long idPatient, UpdateDirectionDTO direccion);

    ResponseEntity<Response> deletePatientById(long idPatient);
}
