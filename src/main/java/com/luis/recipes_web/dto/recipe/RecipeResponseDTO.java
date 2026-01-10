package com.luis.recipes_web.dto.recipe;

import java.time.LocalDateTime;

public class RecipeResponseDTO {

    private Long idRecipe;
    private LocalDateTime fechaCreacion;
    private String observaciones;
    private Boolean activa;

    private Long idPart;
    private String codigoPartNumber;
    private String nombrePartNumber;

    // NUEVO
    private Integer taskCount;

    public Long getIdRecipe() {
        return idRecipe;
    }

    public void setIdRecipe(Long idRecipe) {
        this.idRecipe = idRecipe;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public Long getIdPart() {
        return idPart;
    }

    public void setIdPart(Long idPart) {
        this.idPart = idPart;
    }

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

    // NUEVO
    public Integer getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Integer taskCount) {
        this.taskCount = taskCount;
    }
}
