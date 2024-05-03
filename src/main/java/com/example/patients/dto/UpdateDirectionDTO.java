package com.example.patients.dto;

public class UpdateDirectionDTO {
    private String direccion;

    public UpdateDirectionDTO() {

    }

    public UpdateDirectionDTO(String direccion) {
        this.direccion = direccion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
