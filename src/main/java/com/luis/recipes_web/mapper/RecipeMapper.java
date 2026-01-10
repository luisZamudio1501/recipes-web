package com.luis.recipes_web.mapper;

import com.luis.recipes_web.dominio.PartNumber;
import com.luis.recipes_web.dominio.Recipe;
import com.luis.recipes_web.dto.recipe.RecipeRequestDTO;
import com.luis.recipes_web.dto.recipe.RecipeResponseDTO;

public class RecipeMapper {

    public static Recipe toEntity(RecipeRequestDTO dto, PartNumber partNumber) {
        Recipe entity = new Recipe();
        entity.setPartNumber(partNumber);
        entity.setFechaCreacion(dto.getFechaCreacion());
        entity.setObservaciones(dto.getObservaciones());
        entity.setActiva(dto.getActiva());
        return entity;
    }

    public static void updateEntity(Recipe entity, RecipeRequestDTO dto, PartNumber partNumber) {
        entity.setPartNumber(partNumber);
        entity.setFechaCreacion(dto.getFechaCreacion());
        entity.setObservaciones(dto.getObservaciones());
        entity.setActiva(dto.getActiva());
    }

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

        // IMPORTANTE: no tocar entity.getTasks() acá (LAZY).
        dto.setTaskCount(0);

        return dto;
    }
}
