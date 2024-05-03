package com.example.patients.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    void savePatientTest() {

        // Crea oaciente
        Patient patient = new Patient("17786044-1", "Gabriela", "Ramírez", "Vargas",
                new Date(), 'F', "Avenida Principal 456",
                "+56998765432", "gabriela.ramirez@example.com");

        // Guarda paciente en BD
        Patient savedPatient = patientRepository.save(patient);

        // Verifica que el id existe y que el rut coincida
        assertNotNull(savedPatient.getIdPaciente());
        assertEquals("17786044-1", savedPatient.getRut());
    }

    @Test
    void getPatientByIdTest() {
        // Llamar al método getById() del repositorio
        Optional<Patient> patient = patientRepository.findById(Long.parseLong("1"));

        // Verificar que el usuario exista y que el email coincida
        assertNotNull(patient.get());
        assertTrue(patient.isPresent());
        assertEquals("12345678-9", patient.get().getRut());
    }

    @Test
    void updatePatientDirectionTest() {
        // Llamar al método getById() del repositorio
        Optional<Patient> user = patientRepository.findById(Long.parseLong("1"));

        // Crear objeto paciente en base al pacient llamado y actualizar direccion
        assertEquals("Calle 123", user.get().getDireccion());
        Patient patientToUpdate = user.get();
        patientToUpdate.setDireccion("Calle Test 123");

        // Guardar actualizacion y validamos que coincida con lo actualizado
        Patient updatedPatient = patientRepository.save(patientToUpdate);
        assertEquals("Calle Test 123", updatedPatient.getDireccion());
    }

}
