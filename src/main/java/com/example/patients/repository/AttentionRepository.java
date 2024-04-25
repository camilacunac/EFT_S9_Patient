package com.example.patients.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.patients.model.Attention;

public interface AttentionRepository extends JpaRepository<Attention, Long> {
    List<Attention> findByPatientIdPaciente(Long idPatient);

    void deleteAllByPatientIdPaciente(Long idPatient);
}
