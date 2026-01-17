package com.luis.recipes_web.dominio;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "recipe_material",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_recipe_material",
                        columnNames = {"id_recipe", "id_material"}
                )
        },
        indexes = {
                @Index(name = "idx_rm_recipe", columnList = "id_recipe"),
                @Index(name = "idx_rm_material", columnList = "id_material"),
                @Index(name = "idx_rm_recipe_material", columnList = "id_recipe,id_material")
        }
)
public class RecipeMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recipe_material", nullable = false)
    private Long idRecipeMaterial;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_recipe", nullable = false,
            foreignKey = @ForeignKey(name = "fk_rm_recipe"))
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_material", nullable = false,
            foreignKey = @ForeignKey(name = "fk_rm_material"))
    private Material material;

    @Column(name = "cantidad_por_unidad", nullable = false, precision = 18, scale = 6)
    private BigDecimal cantidadPorUnidad;

    @Column(name = "merma_porcentaje", nullable = false, precision = 7, scale = 4)
    private BigDecimal mermaPorcentaje;

    @Column(name = "nota", length = 255)
    private String nota;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Long getIdRecipeMaterial() {
        return idRecipeMaterial;
    }

    public void setIdRecipeMaterial(Long idRecipeMaterial) {
        this.idRecipeMaterial = idRecipeMaterial;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
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

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
