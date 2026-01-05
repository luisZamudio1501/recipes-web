package com.luis.recipes_web.dominio;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name = "recipe",
        indexes = {
                @Index(name = "idx_recipe_part", columnList = "id_part"),
                @Index(name = "idx_recipe_activa", columnList = "activa"),
                @Index(name = "idx_recipe_part_activa", columnList = "id_part, activa")
        }
)
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recipe", nullable = false)
    private Long idRecipe;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_part",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_recipe_part")
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private PartNumber partNumber;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "activa", nullable = false)
    private Boolean activa;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(
            mappedBy = "recipe",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<RecipeTask> tasks;

    public Recipe() {
    }

    public Long getIdRecipe() {
        return idRecipe;
    }

    public void setIdRecipe(Long idRecipe) {
        this.idRecipe = idRecipe;
    }

    public PartNumber getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(PartNumber partNumber) {
        this.partNumber = partNumber;
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

    public List<RecipeTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<RecipeTask> tasks) {
        this.tasks = tasks;
    }

    @PrePersist
    protected void onCreate() {
        if (this.fechaCreacion == null) this.fechaCreacion = LocalDateTime.now();
        if (this.activa == null) this.activa = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recipe)) return false;
        Recipe that = (Recipe) o;
        return idRecipe != null && idRecipe.equals(that.idRecipe);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
