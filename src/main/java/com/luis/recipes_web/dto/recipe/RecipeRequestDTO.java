package com.luis.recipes_web.dto.recipe;

import java.time.LocalDateTime;

public class RecipeRequestDTO {

    private Long idPart;
    private LocalDateTime fechaCreacion;
    private String observaciones;
    private Boolean activa;

    public Long getIdPart() { return idPart; }
    public void setIdPart(Long idPart) { this.idPart = idPart; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public Boolean getActiva() { return activa; }
    public void setActiva(Boolean activa) { this.activa = activa; }
}
