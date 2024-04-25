package com.example.patients.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.patients.model.Patient;
import com.example.patients.model.Response;
import com.example.patients.repository.PatientRepository;

@ExtendWith(MockitoExtension.class)
class PatientServceTest {

        @InjectMocks
        private PatienteServiceImpl patientService;

        @Mock
        private PatientRepository patientRepository;

        @Test
        void getAllPatientsTest() {
                // Crear una lista de pacientes simulada
                List<Patient> patientList = new ArrayList<>();
                patientList.add(new Patient("12345678-9", "Juan", "Pérez", "González", new Date(), 'M',
                                "Calle Linda 38", "+56987654321", "juanperez@example.com"));
                patientList.add(new Patient("98765432-1", "María", "López", "Hernández", new Date(), 'F',
                                "Avenida Principal 456", "+56912345678", "marialopez@example.com"));
                patientList.add(new Patient("45678901-2", "Pedro", "Ramírez", "Díaz", new Date(), 'M',
                                "Calle Secundaria 789", "+56876543210", "pedroramirez@example.com"));
                patientList.add(new Patient("23456789-0", "Ana", "Gómez", "Martínez", new Date(), 'F',
                                "Avenida Central 321", "+56901234567", "anagomez@example.com"));
                patientList.add(new Patient("34567890-1", "Luis", "Fernández", "Ruiz", new Date(), 'M',
                                "Calle Principal 987", "+56923456789", "luisfernandez@example.com"));

                // Simular el comportamiento del PatientRepository.findAll() para devolver la
                // lista
                // simulada
                when(patientRepository.findAll()).thenReturn(patientList);

                // Llamar al método del servicio
                List<Patient> result = patientService.getAllPatients();

                // Verificar que el método devuelve la lista esperada
                assertEquals(patientList.size(), result.size());
                assertEquals(patientList.get(0).getRut(), result.get(0).getRut());
                assertEquals(patientList.get(1).getRut(), result.get(1).getRut());
                assertEquals(patientList.get(2).getRut(), result.get(2).getRut());
                assertEquals(patientList.get(3).getRut(), result.get(3).getRut());
                assertEquals(patientList.get(4).getRut(), result.get(4).getRut());
        }

        @Test
        void successCreatePatientTest() {
                // Configurar el PacienteRepository para simular que el rut no está registrado
                when(patientRepository.existsByRut("12983018-2")).thenReturn(false);

                // Crear un nuevo paciente para registrar
                Patient newPatient = new Patient("12983018-2", "Jorge", "Gutierrez", "Gonzales", new Date(), 'M',
                                "Calle 123",
                                "+56923837123", "testjorge@gmail.com");

                // Simular el guardado exitoso del paciente
                when(patientRepository.save(newPatient)).thenReturn(newPatient);

                // Llamar al método del servicio
                ResponseEntity<Response> response = patientService.addPatient(newPatient);

                // Verificar que se devuelve una respuesta exitosa
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertEquals("success", response.getBody().getState());
                assertEquals(newPatient, response.getBody().getResponse());
                assertTrue(response.getBody().getError().isEmpty());
        }

}
