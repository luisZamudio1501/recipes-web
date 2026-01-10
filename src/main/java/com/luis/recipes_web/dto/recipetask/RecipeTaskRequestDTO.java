package com.luis.recipes_web.dto.recipetask;

import jakarta.validation.constraints.NotNull;

public class RecipeTaskRequestDTO {

    @NotNull
    private Integer orden;

    private String observaciones;

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
