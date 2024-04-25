package com.example.patients.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.patients.model.Patient;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PatientRepositoryTest {
    @Autowired
    private PatientRepository patientRepository;

    @Test
    void getAllPatientsTest() {
        // Llamar al método findAll() del repositorio
        List<Patient> userList = patientRepository.findAll();

        // Verificar que la lista de pacientes no esté vacía
        assertNotNull(userList);
        assertFalse(userList.isEmpty());
    }

    @Test
    void saveUserTest() {

        // Crea oaciente
        Patient patient = new Patient("17786044-1", "Gabriela", "Ramírez", "Vargas",
                "20-09-1997", 'F', "Avenida Principal 456",
                "+56998765432", "gabriela.ramirez@example.com");

        // Guarda paciente en BD
        Patient savedPatient = patientRepository.save(patient);

        // Verifica que el id existe y que el rut coincida
        assertNotNull(savedPatient.getIdPaciente());
        assertEquals("17786044-1", savedPatient.getRut());
    }

}
