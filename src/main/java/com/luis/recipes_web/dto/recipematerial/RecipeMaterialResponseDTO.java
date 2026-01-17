package com.luis.recipes_web.dto.recipematerial;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RecipeMaterialResponseDTO {

    private Long idRecipeMaterial;
    private Long idRecipe;

    private Long idMaterial;
    private String codigoMaterial;
    private String nombreMaterial;

    private BigDecimal cantidadPorUnidad;
    private BigDecimal mermaPorcentaje;
    private String nota;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /* =========================
       Getters y Setters
       ========================= */

    public Long getIdRecipeMaterial() {
        return idRecipeMaterial;
    }

    public void setIdRecipeMaterial(Long idRecipeMaterial) {
        this.idRecipeMaterial = idRecipeMaterial;
    }

    public Long getIdRecipe() {
        return idRecipe;
    }

    public void setIdRecipe(Long idRecipe) {
        this.idRecipe = idRecipe;
    }

    public Long getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(Long idMaterial) {
        this.idMaterial = idMaterial;
    }

    public String getCodigoMaterial() {
        return codigoMaterial;
    }

    public void setCodigoMaterial(String codigoMaterial) {
        this.codigoMaterial = codigoMaterial;
    }

    public String getNombreMaterial() {
        return nombreMaterial;
    }

    public void setNombreMaterial(String nombreMaterial) {
        this.nombreMaterial = nombreMaterial;
    }

    public BigDecimal getCantidadPorUnidad() {
        return cantidadPorUnidad;
    }

    public void setCantidadPorUnidad(BigDecimal cantidadPorUnidad) {
        this.cantidadPorUnidad = cantidadPorUnidad;
    }

    public BigDecimal getMermaPorcentaje() {
        return mermaPorcentaje;
    }

    public void setMermaPorcentaje(BigDecimal mermaPorcentaje) {
        this.mermaPorcentaje = mermaPorcentaje;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
