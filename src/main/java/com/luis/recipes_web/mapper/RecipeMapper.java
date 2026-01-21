package com.luis.recipes_web.mapper;

import com.luis.recipes_web.dominio.PartNumber;
import com.luis.recipes_web.dominio.Recipe;
import com.luis.recipes_web.dto.recipe.RecipeRequestDTO;
import com.luis.recipes_web.dto.recipe.RecipeResponseDTO;

import java.time.LocalDateTime;

public class RecipeMapper {

    // CREATE
    public static Recipe toEntity(RecipeRequestDTO dto, PartNumber partNumber) {
        Recipe entity = new Recipe();
        entity.setPartNumber(partNumber);
        entity.setFechaCreacion(LocalDateTime.now()); // backend
        entity.setObservaciones(dto.getObservaciones());
        entity.setActiva(dto.getActiva());
        return entity;
    }

    // UPDATE
    public static void updateEntity(Recipe entity, RecipeRequestDTO dto, PartNumber partNumber) {
        entity.setPartNumber(partNumber);
        // NO tocar fechaCreacion en update
        entity.setObservaciones(dto.getObservaciones());
        entity.setActiva(dto.getActiva());
    }

    // RESPONSE
    public static RecipeResponseDTO toResponse(Recipe entity) {
        RecipeResponseDTO dto = new RecipeResponseDTO();
        dto.setIdRecipe(entity.getIdRecipe());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setObservaciones(entity.getObservaciones());
        dto.setActiva(entity.getActiva());

        if (entity.getPartNumber() != null) {
            dto.setIdPart(entity.getPartNumber().getIdPart());
            dto.setCodigoPartNumber(entity.getPartNumber().getCodigoPartNumber());
            dto.setNombrePartNumber(entity.getPartNumber().getNombrePartNumber());
        }

        // IMPORTANTE: no tocar tasks (LAZY)
        dto.setTaskCount(0);

        return dto;
    }
}
