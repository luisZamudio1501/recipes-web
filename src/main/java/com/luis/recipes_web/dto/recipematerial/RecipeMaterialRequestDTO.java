package com.luis.recipes_web.dto.recipematerial;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class RecipeMaterialRequestDTO {

    @NotNull
    private Long idRecipe;

    @NotNull
    private Long idMaterial;

    @NotNull
    @DecimalMin(value = "0.000000", inclusive = false)
    private BigDecimal cantidadPorUnidad;

    @NotNull
    @DecimalMin(value = "0.0000", inclusive = true)
    private BigDecimal mermaPorcentaje;

    private String nota;

    public Long getIdRecipe() { return idRecipe; }
    public void setIdRecipe(Long idRecipe) { this.idRecipe = idRecipe; }

    public Long getIdMaterial() { return idMaterial; }
    public void setIdMaterial(Long idMaterial) { this.idMaterial = idMaterial; }

    public BigDecimal getCantidadPorUnidad() { return cantidadPorUnidad; }
    public void setCantidadPorUnidad(BigDecimal cantidadPorUnidad) { this.cantidadPorUnidad = cantidadPorUnidad; }

    public BigDecimal getMermaPorcentaje() { return mermaPorcentaje; }
    public void setMermaPorcentaje(BigDecimal mermaPorcentaje) { this.mermaPorcentaje = mermaPorcentaje; }

    public String getNota() { return nota; }
    public void setNota(String nota) { this.nota = nota; }
}
