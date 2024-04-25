package com.example.patients.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.patients.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    boolean existsByRut(String rut);
}
