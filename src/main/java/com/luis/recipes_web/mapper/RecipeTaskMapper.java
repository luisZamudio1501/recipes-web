package com.luis.recipes_web.mapper;

import com.luis.recipes_web.dominio.Recipe;
import com.luis.recipes_web.dominio.RecipeTask;
import com.luis.recipes_web.dto.recipetask.RecipeTaskRequestDTO;
import com.luis.recipes_web.dto.recipetask.RecipeTaskResponseDTO;

public class RecipeTaskMapper {

    // =========================
    // REQUEST → ENTITY
    // =========================
    public static RecipeTask toEntity(RecipeTaskRequestDTO dto, Recipe recipe) {
        RecipeTask entity = new RecipeTask();
        entity.setRecipe(recipe);
        entity.setOrden(dto.getOrden());
        entity.setObservaciones(dto.getObservaciones());
        return entity;
    }

    // =========================
    // UPDATE ENTITY
    // =========================
    public static void updateEntity(RecipeTask entity, RecipeTaskRequestDTO dto) {
        entity.setOrden(dto.getOrden());
        entity.setObservaciones(dto.getObservaciones());
    }

    // =========================
    // ENTITY → RESPONSE
    // =========================
    public static RecipeTaskResponseDTO toResponse(RecipeTask entity) {
        RecipeTaskResponseDTO dto = new RecipeTaskResponseDTO();
        dto.setIdRecipeTask(entity.getIdRecipeTask());
        dto.setOrden(entity.getOrden());
        dto.setObservaciones(entity.getObservaciones());
        return dto;
    }
}
