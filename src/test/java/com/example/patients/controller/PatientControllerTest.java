package com.example.patients.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.patients.dto.UpdateDirectionDTO;
import com.example.patients.model.Patient;
import com.example.patients.model.Response;
import com.example.patients.services.PatienteServiceImpl;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private PatienteServiceImpl patientServiceMock;

        @Test
        void getAllPatients() throws Exception {
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

                // Simular el comportamiento del servicio para devolver la lista simulada de
                // pacientes
                when(patientServiceMock.getAllPatients()).thenReturn(patientList);

                // Ejecutar la solicitud GET al endpoint "/pacientes" y verificar la respuesta
                mockMvc.perform(MockMvcRequestBuilders.get("/pacientes"))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].idPaciente")
                                                .value(patientList.get(0).getIdPaciente()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email")
                                                .value(patientList.get(0).getEmail()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].rut")
                                                .value(patientList.get(0).getRut()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].idPaciente")
                                                .value(patientList.get(1).getIdPaciente()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].email")
                                                .value(patientList.get(1).getEmail()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].rut")
                                                .value(patientList.get(1).getRut()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[2].idPaciente")
                                                .value(patientList.get(2).getIdPaciente()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[2].email")
                                                .value(patientList.get(2).getEmail()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[2].rut")
                                                .value(patientList.get(2).getRut()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[3].idPaciente")
                                                .value(patientList.get(3).getIdPaciente()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[3].email")
                                                .value(patientList.get(3).getEmail()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[3].rut")
                                                .value(patientList.get(3).getRut()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[4].idPaciente")
                                                .value(patientList.get(4).getIdPaciente()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[4].email")
                                                .value(patientList.get(4).getEmail()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[4].rut")
                                                .value(patientList.get(4).getRut()));
        }

        @Test
        void createNewPatientTest() throws Exception {
                // Crear un nuevo usuario para registrar
                Patient newPatient = new Patient(
                                "17786044-1",
                                "Gabriela",
                                "Ramírez",
                                "Vargas",
                                new Date(1997 - 12 - 10),
                                'F',
                                "Avenida Principal 456",
                                "+56998765432",
                                "gabriela.ramirez@example.com");
                Response successResponse = new Response("success", newPatient, "");

                // Simular el servicio para devolver la respuesta exitosa al registrar el
                // usuario
                ResponseEntity<Response> responseEntity = ResponseEntity.ok(successResponse);
                when(patientServiceMock.addPatient(any(Patient.class))).thenReturn(responseEntity);

                // Ejecutar la solicitud POST al endpoint "/register" con el nuevo usuario y
                // verificar la respuesta
                mockMvc.perform(MockMvcRequestBuilders.post("/agregar-paciente")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                                "{\"rut\": \"17786044-1\", \"nombre\": \"Gabriela\", \"apellidoPaterno\": \"Ramírez\", \"apellidoMaterno\": \"Vargas\", \"fechaNacimiento\": \"1997-12-10\", \"genero\": \"F\", \"direccion\": \"Avenida Principal 456\", \"telefono\": \"+56998765432\", \"email\": \"gabriela.ramirez@example.com\"}"))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("success"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value(""))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.response.email")
                                                .value(newPatient.getEmail()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.response.rut").value(newPatient.getRut()));
        }

        @Test
        void getPatientById() throws Exception {
                // Arrange
                Long patientId = 1L;
                Patient patient = new Patient("12345678-9", "Juan", "Pérez", "González", new Date(), 'M',
                                "Calle 123", "+56987654321", "juanperez@example.com");
                Response response = new Response("success", patient, "");
                ResponseEntity<Response> serviceResponse = ResponseEntity.ok(response);

                // Act
                when(patientServiceMock.getPatientById(patientId)).thenReturn(serviceResponse);

                // Assert
                mockMvc.perform(MockMvcRequestBuilders.get("/paciente/{id}", patientId))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("success"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value(""))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.response.rut").value(patient.getRut()));
        }

        @Test
        void updatePatientDirectionTest() throws Exception {
                // Arange
                Long patientId = 1L;
                Patient patient = new Patient("12345678-9", "Juan", "Pérez", "González", new Date(), 'M',
                                "Calle 123", "+56987654321", "juanperez@example.com");
                patient.setDireccion("Calle Test 123");
                Response response = new Response("success", patient, "");
                ResponseEntity<Response> serviceResponse = ResponseEntity.ok(response);

                // Act
                when(patientServiceMock.updatePatientDirection(eq(patientId), any(UpdateDirectionDTO.class)))
                                .thenReturn(serviceResponse);

                // Assert
                mockMvc.perform(MockMvcRequestBuilders.put("/paciente/{idPatient}/actualizar-direccion", patientId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{ \"direccion\": \"Calle Test 123\" }"))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("success"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.response.direccion")
                                                .value(patient.getDireccion()));
        }

}
