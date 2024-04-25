package com.example.patients.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.patients.model.Consultation;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    List<Consultation> findByPatientIdPaciente(Long idPatient);

    void deleteAllByPatientIdPaciente(Long idPatient);
}
