package com.luis.recipes_web.mapper;

import com.luis.recipes_web.dominio.RecipeMaterial;
import com.luis.recipes_web.dto.recipematerial.RecipeMaterialResponseDTO;

public class RecipeMaterialMapper {

    private RecipeMaterialMapper() {
    }

    public static RecipeMaterialResponseDTO toResponseDTO(RecipeMaterial rm) {

        RecipeMaterialResponseDTO dto = new RecipeMaterialResponseDTO();
        dto.setIdRecipeMaterial(rm.getIdRecipeMaterial());

        if (rm.getRecipe() != null) {
            dto.setIdRecipe(rm.getRecipe().getIdRecipe());
        }

        if (rm.getMaterial() != null) {
            dto.setIdMaterial(rm.getMaterial().getIdMaterial());
            dto.setCodigoMaterial(rm.getMaterial().getCodigoMaterial());
            dto.setNombreMaterial(rm.getMaterial().getNombre());
        }

        dto.setCantidadPorUnidad(rm.getCantidadPorUnidad());
        dto.setMermaPorcentaje(rm.getMermaPorcentaje());
        dto.setNota(rm.getNota());
        dto.setCreatedAt(rm.getCreatedAt());
        dto.setUpdatedAt(rm.getUpdatedAt());

        return dto;
    }
}
