package com.luis.recipes_web.dto.material;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MaterialRequestDTO {

    @NotBlank(message = "codigoMaterial es obligatorio")
    @Size(max = 50, message = "codigoMaterial debe tener como máximo 50 caracteres")
    private String codigoMaterial;

    @NotBlank(message = "nombreMaterial es obligatorio")
    @Size(max = 120, message = "nombreMaterial debe tener como máximo 120 caracteres")
    private String nombreMaterial;

    @NotBlank(message = "unidadMedida es obligatorio")
    @Size(max = 20, message = "unidadMedida debe tener como máximo 20 caracteres")
    private String unidadMedida;

    @Size(max = 255, message = "descripcion debe tener como máximo 255 caracteres")
    private String descripcion;

    @NotNull(message = "activo es obligatorio")
    private Boolean activo;

    public String getCodigoMaterial() { return codigoMaterial; }
    public void setCodigoMaterial(String codigoMaterial) { this.codigoMaterial = codigoMaterial; }

    public String getNombreMaterial() { return nombreMaterial; }
    public void setNombreMaterial(String nombreMaterial) { this.nombreMaterial = nombreMaterial; }

    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
