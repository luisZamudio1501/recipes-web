package com.luis.recipes_web.dominio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "recipe_task",
        indexes = {
                @Index(name = "idx_task_process", columnList = "id_process"),
                @Index(name = "idx_task_drawing", columnList = "id_drawing"),
                @Index(name = "idx_task_proc_desc", columnList = "id_process_description"),
                @Index(name = "idx_recipe_task_recipe", columnList = "id_recipe"),
                @Index(name = "idx_recipe_task_recipe_orden", columnList = "id_recipe, orden")
        }
)
public class RecipeTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recipe_task", nullable = false)
    private Long idRecipeTask;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_recipe",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_task_recipe")
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Recipe recipe;

    // En este hito: mapeo simple por id (para no crear 3 entidades nuevas todavía)

    @NotNull(message = "idProcess es obligatorio")
    @Min(value = 1, message = "idProcess debe ser >= 1")
    @Column(name = "id_process", nullable = false)
    private Long idProcess;

    @NotNull(message = "idProcessDescription es obligatorio")
    @Min(value = 1, message = "idProcessDescription debe ser >= 1")
    @Column(name = "id_process_description", nullable = false)
    private Long idProcessDescription;

    @NotNull(message = "idDrawing es obligatorio")
    @Min(value = 1, message = "idDrawing debe ser >= 1")
    @Column(name = "id_drawing", nullable = false)
    private Long idDrawing;

    @NotNull(message = "orden es obligatorio")
    @Min(value = 1, message = "orden debe ser >= 1")
    @Column(name = "orden", nullable = false)
    private Integer orden;

    @NotNull(message = "tiempoMinutos es obligatorio")
    @Min(value = 0, message = "tiempoMinutos debe ser >= 0")
    @Column(name = "tiempo_minutos", nullable = false)
    private Integer tiempoMinutos;

    // No @NotNull: si viene null, se setea true en @PrePersist
    @Column(name = "es_correlativa", nullable = false)
    private Boolean esCorrelativa;

    @Size(max = 2000, message = "observaciones no puede superar 2000 caracteres")
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public RecipeTask() {
    }

    public Long getIdRecipeTask() {
        return idRecipeTask;
    }

    public void setIdRecipeTask(Long idRecipeTask) {
        this.idRecipeTask = idRecipeTask;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Long getIdProcess() {
        return idProcess;
    }

    public void setIdProcess(Long idProcess) {
        this.idProcess = idProcess;
    }

    public Long getIdProcessDescription() {
        return idProcessDescription;
    }

    public void setIdProcessDescription(Long idProcessDescription) {
        this.idProcessDescription = idProcessDescription;
    }

    public Long getIdDrawing() {
        return idDrawing;
    }

    public void setIdDrawing(Long idDrawing) {
        this.idDrawing = idDrawing;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Integer getTiempoMinutos() {
        return tiempoMinutos;
    }

    public void setTiempoMinutos(Integer tiempoMinutos) {
        this.tiempoMinutos = tiempoMinutos;
    }

    public Boolean getEsCorrelativa() {
        return esCorrelativa;
    }

    public void setEsCorrelativa(Boolean esCorrelativa) {
        this.esCorrelativa = esCorrelativa;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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
    protected void onCreate() {
        if (this.esCorrelativa == null) this.esCorrelativa = true;
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
        if (!(o instanceof RecipeTask)) return false;
        RecipeTask that = (RecipeTask) o;
        return idRecipeTask != null && idRecipeTask.equals(that.idRecipeTask);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
