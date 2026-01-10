package com.luis.recipes_web.dto.recipetask;

public class RecipeTaskResponseDTO {

    private Long idRecipeTask;
    private Integer orden;
    private String observaciones;

    public Long getIdRecipeTask() {
        return idRecipeTask;
    }

    public void setIdRecipeTask(Long idRecipeTask) {
        this.idRecipeTask = idRecipeTask;
    }

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

