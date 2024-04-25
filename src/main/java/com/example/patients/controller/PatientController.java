package com.example.patients.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.patients.dto.AttentionDTO;
import com.example.patients.dto.ConsultationDTO;
import com.example.patients.dto.UpdateDirectionDTO;
import com.example.patients.model.Attention;
import com.example.patients.model.Consultation;
import com.example.patients.model.Patient;
import com.example.patients.model.Response;
import com.example.patients.services.PatientService;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

@RestController
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping("/pacientes")
    public List<EntityModel<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();

        return patients.stream()
                .map((Patient patient) -> EntityModel.of(patient,
                        WebMvcLinkBuilder.linkTo(PatientController.class).slash("paciente")
                                .slash(patient.getIdPaciente())
                                .withSelfRel()))
                .collect(Collectors.toList());
    }

    @GetMapping("/paciente/{id}")
    public ResponseEntity<EntityModel<Response>> getPatientById(@PathVariable Long id) {
        ResponseEntity<Response> responseEntity = patientService.getPatientById(id);

        EntityModel<Response> responseResource = EntityModel.of(responseEntity.getBody());

        if (responseEntity.getBody().getError().equals("")) {
            responseResource
                    .add(WebMvcLinkBuilder.linkTo(PatientController.class).slash("paciente").slash(id)
                            .withSelfRel());
        }
        return ResponseEntity.status(responseEntity.getStatusCode())
                .body(responseResource);
    }

    @GetMapping("/consultas/{idPatient}")
    public List<EntityModel<Consultation>> getConsultationsByPatientId(@PathVariable Long idPatient) {
        List<Consultation> consultations = patientService.getAllConsultationsByPatientId(idPatient);
        return consultations.stream()
                .map((Consultation consultation) -> EntityModel.of(consultation,
                        WebMvcLinkBuilder.linkTo(PatientController.class).slash("consultas").slash(idPatient)
                                .withSelfRel()))
                .collect(Collectors.toList());
    }

    @GetMapping("/atenciones/{idPatient}")
    public List<EntityModel<Attention>> getAttentionsByPatientId(@PathVariable Long idPatient) {
        List<Attention> attentions = patientService.getAllAttentionsByPatientId(idPatient);
        return attentions.stream()
                .map((Attention attention) -> EntityModel.of(attention,
                        WebMvcLinkBuilder.linkTo(PatientController.class).slash("atenciones").slash(idPatient)
                                .withSelfRel()))
                .collect(Collectors.toList());
    }

    @PostMapping("/agregar-paciente")
    public ResponseEntity<EntityModel<Response>> addPatient(@RequestBody Patient patient) {
        ResponseEntity<Response> responseEntity = patientService.addPatient(patient);

        EntityModel<Response> responseResource = EntityModel.of(responseEntity.getBody());

        responseResource
                .add(WebMvcLinkBuilder.linkTo(PatientController.class).slash("pacientes").withRel("all-patients"));
        if (responseEntity.getBody().getError().equals("")) {
            responseResource
                    .add(WebMvcLinkBuilder.linkTo(PatientController.class).slash("paciente")
                            .slash(patient.getIdPaciente())
                            .withSelfRel());
        }
        return ResponseEntity.status(responseEntity.getStatusCode())
                .body(responseResource);

    }

    @PostMapping("/agregar-consulta/{idPatient}")
    public ResponseEntity<EntityModel<Response>> addConsultForPatient(@PathVariable Long idPatient,
            @RequestBody ConsultationDTO consulta) {
        ResponseEntity<Response> responseEntity = patientService.addConsultForPatient(idPatient, consulta);

        EntityModel<Response> responseResource = EntityModel.of(responseEntity.getBody());

        if (responseEntity.getBody().getError().equals("")) {
            responseResource
                    .add(WebMvcLinkBuilder.linkTo(PatientController.class).slash("paciente").slash(idPatient)
                            .withRel("patient"));
            responseResource
                    .add(WebMvcLinkBuilder.linkTo(PatientController.class).slash("consultas").slash(idPatient)
                            .withRel("patient-consultations"));
        }
        return ResponseEntity.status(responseEntity.getStatusCode())
                .body(responseResource);
    }

    @PostMapping("/agregar-atencion/{idPatient}")
    public ResponseEntity<EntityModel<Response>> addAttetionForPatient(@PathVariable Long idPatient,
            @RequestBody AttentionDTO atencion) {
        ResponseEntity<Response> responseEntity = patientService.addAttentionForPatient(idPatient, atencion);

        EntityModel<Response> responseResource = EntityModel.of(responseEntity.getBody());

        if (responseEntity.getBody().getError().equals("")) {
            responseResource
                    .add(WebMvcLinkBuilder.linkTo(PatientController.class).slash("paciente").slash(idPatient)
                            .withRel("patient"));
            responseResource
                    .add(WebMvcLinkBuilder.linkTo(PatientController.class).slash("atenciones").slash(idPatient)
                            .withRel("patient-attentions"));
        }
        return ResponseEntity.status(responseEntity.getStatusCode())
                .body(responseResource);
    }

    @PutMapping("/paciente/{idPatient}/actualizar-direccion")
    public ResponseEntity<EntityModel<Response>> updatePatientDirection(@PathVariable Long idPatient,
            @RequestBody UpdateDirectionDTO direccion) {
        ResponseEntity<Response> responseEntity = patientService.updatePatientDirection(idPatient, direccion);

        EntityModel<Response> responseResource = EntityModel.of(responseEntity.getBody());
        responseResource
                .add(WebMvcLinkBuilder.linkTo(PatientController.class).slash("pacientes").withRel("all-patients"));
        if (responseEntity.getBody().getError().equals("")) {
            responseResource
                    .add(WebMvcLinkBuilder.linkTo(PatientController.class).slash("paciente").slash(idPatient)
                            .withSelfRel());
        }

        return ResponseEntity.status(responseEntity.getStatusCode())
                .body(responseResource);
    }

    @DeleteMapping("/eliminar-paciente/{idPatient}")
    public ResponseEntity<EntityModel<Response>> deletePatientById(@PathVariable Long idPatient) {
        ResponseEntity<Response> responseEntity = patientService.deletePatientById(idPatient);

        EntityModel<Response> responseResource = EntityModel.of(responseEntity.getBody());
        responseResource
                .add(WebMvcLinkBuilder.linkTo(PatientController.class).slash("/pacientes").withRel("all-patients"));

        return ResponseEntity.status(responseEntity.getStatusCode())
                .body(responseResource);
    }
}
