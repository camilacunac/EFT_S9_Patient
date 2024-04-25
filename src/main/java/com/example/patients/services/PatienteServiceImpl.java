package com.example.patients.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.patients.dto.AttentionDTO;
import com.example.patients.dto.ConsultationDTO;
import com.example.patients.dto.UpdateDirectionDTO;
import com.example.patients.model.Attention;
import com.example.patients.model.Consultation;
import com.example.patients.model.Patient;
import com.example.patients.model.Response;
import com.example.patients.repository.AttentionRepository;
import com.example.patients.repository.ConsultationRepository;
import com.example.patients.repository.PatientRepository;

import jakarta.transaction.Transactional;

@Service
public class PatienteServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private ConsultationRepository consultationRepository;
    @Autowired
    private AttentionRepository attentionRepository;

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public ResponseEntity<Response> getPatientById(Long id) {
        Response res;
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isPresent()) {
            res = new Response("success", patient, "");
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        res = new Response("error", "", "No se encontro ningun paciente con ese id");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @Override
    public List<Consultation> getAllConsultationsByPatientId(Long idPatient) {
        return consultationRepository.findByPatientIdPaciente(idPatient);
    }

    @Override
    public List<Attention> getAllAttentionsByPatientId(Long idPatient) {
        return attentionRepository.findByPatientIdPaciente(idPatient);
    }

    @Override
    public ResponseEntity<Response> addPatient(Patient patient) {
        Response res;
        try {
            if (patientRepository.existsByRut(patient.getRut())) {
                throw new IllegalArgumentException("El rut ya está registrado");
            }

            if (!validateRut(patient.getRut())) {
                throw new IllegalArgumentException("El rut ingresado no es valido");
            }

            if (!validateEmail(patient.getEmail())) {
                throw new IllegalArgumentException("El email ingresado no es valido");
            }

            Patient savedPatient = patientRepository.save(patient);
            res = new Response("success", savedPatient, "");
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (IllegalArgumentException e) {
            res = new Response("error", "", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        } catch (Exception e) {
            res = new Response("error", "", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @Override
    public ResponseEntity<Response> addConsultForPatient(Long idPatient, ConsultationDTO data) {
        Response res;
        try {
            Optional<Patient> patient = patientRepository.findById(idPatient);
            if (patient.isPresent()) {
                Consultation consulta = new Consultation();
                consulta.setDiagnostico(data.getDiagnostico());
                consulta.setFecha(data.getFecha());
                consulta.setMotivo(data.getMotivo());
                consulta.setTratamiento(data.getTratamiento());
                consulta.setPatient(patient.get());
                Consultation savedConsulta = consultationRepository.save(consulta);
                res = new Response("success", savedConsulta, "");
                return ResponseEntity.status(HttpStatus.OK).body(res);
            }
            res = new Response("error", "", "No se encontro ningun paciente con ese id");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        } catch (Exception e) {
            res = new Response("error", "", "Error al procesar la solicitud");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @Override
    public ResponseEntity<Response> addAttentionForPatient(long idPatient, AttentionDTO data) {
        Response res;
        try {
            Optional<Patient> patient = patientRepository.findById(idPatient);
            if (patient.isPresent()) {
                Attention atencion = new Attention();
                atencion.setDiagnostico(data.getDiagnostico());
                atencion.setFecha(data.getFecha());
                atencion.setTipo(data.getTipo());
                atencion.setTratamiento(data.getTratamiento());
                atencion.setPatient(patient.get());
                Attention savedAttention = attentionRepository.save(atencion);
                res = new Response("success", savedAttention, "");
                return ResponseEntity.status(HttpStatus.OK).body(res);
            }
            res = new Response("error", "", "No se encontro ningun paciente con ese id");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        } catch (Exception e) {
            res = new Response("error", "", "Error al procesar la solicitud");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @Override
    public ResponseEntity<Response> updatePatientDirection(long idPatient, UpdateDirectionDTO direccion) {
        Response res;
        try {
            Optional<Patient> patient = patientRepository.findById(idPatient);
            if (patient.isPresent()) {
                Patient updatedPatient = patient.get();
                updatedPatient.setDireccion(direccion.getDireccion());
                String[] partesFechaNac = updatedPatient.getFechaNacimiento().substring(0, 10).split("-");
                String invertedFechaNac = partesFechaNac[2] + "-" + partesFechaNac[1] + "-" + partesFechaNac[0];
                updatedPatient.setFechaNacimiento(invertedFechaNac);
                Patient savedPatient = patientRepository.save(updatedPatient);
                res = new Response("success", savedPatient, "");
                return ResponseEntity.status(HttpStatus.OK).body(res);
            }
            res = new Response("error", "", "No se encontro ningun paciente con ese id");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        } catch (Exception e) {
            res = new Response("error", "", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Response> deletePatientById(long idPatient) {
        Response res;
        try {
            Optional<Patient> patient = patientRepository.findById(idPatient);
            if (patient.isPresent()) {
                Patient foundPatient = patient.get();
                consultationRepository.deleteAllByPatientIdPaciente(idPatient);
                attentionRepository.deleteAllByPatientIdPaciente(idPatient);
                patientRepository.deleteById(idPatient);
                res = new Response("success", "Paciente con el rut: " + foundPatient.getRut() + " eliminado con exito",
                        "");
                return ResponseEntity.status(HttpStatus.OK).body(res);
            }
            res = new Response("error", "", "No se encontro ningun paciente con ese id");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        } catch (Exception e) {
            res = new Response("error", e, "Error al procesar la solicitud");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    public static boolean validateEmail(String email) {
        String patron = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(patron);
        return pattern.matcher(email).matches();
    }

    public static boolean validateRut(String rutCompleto) {
        if (!rutCompleto.matches("^[0-9]+-[0-9kK]{1}$"))
            return false;
        String[] tmp = rutCompleto.split("-");
        String digv = tmp[1];
        String rut = tmp[0];
        if (digv.equalsIgnoreCase("K"))
            digv = "k";
        return dv(rut).equals(digv);
    }

    private static String dv(String T) {
        int M = 0;
        int S = 1;
        for (int i = T.length() - 1; i >= 0; i--) {
            S = (S + Integer.parseInt(String.valueOf(T.charAt(i))) * (9 - M++ % 6)) % 11;
        }
        return (S != 0) ? String.valueOf(S - 1) : "k";
    }
}
