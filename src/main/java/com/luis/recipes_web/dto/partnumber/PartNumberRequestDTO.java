package com.luis.recipes_web.dto.partnumber;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PartNumberRequestDTO {

    @NotBlank(message = "codigoPartNumber es obligatorio")
    @Size(max = 50, message = "codigoPartNumber no puede superar 50 caracteres")
    private String codigoPartNumber;

    @NotBlank(message = "nombrePartNumber es obligatorio")
    @Size(max = 255, message = "nombrePartNumber no puede superar 255 caracteres")
    private String nombrePartNumber;

    @NotNull(message = "activo es obligatorio")
    private Boolean activo;

    public String getCodigoPartNumber() {
        return codigoPartNumber;
    }

    public void setCodigoPartNumber(String codigoPartNumber) {
        this.codigoPartNumber = codigoPartNumber;
    }

    public String getNombrePartNumber() {
        return nombrePartNumber;
    }

    public void setNombrePartNumber(String nombrePartNumber) {
        this.nombrePartNumber = nombrePartNumber;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
