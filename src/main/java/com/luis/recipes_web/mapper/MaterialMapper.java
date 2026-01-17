package com.luis.recipes_web.mapper;

import com.luis.recipes_web.dominio.Material;
import com.luis.recipes_web.dto.material.MaterialRequestDTO;
import com.luis.recipes_web.dto.material.MaterialResponseDTO;

public class MaterialMapper {

    private MaterialMapper() {}

    public static MaterialResponseDTO toResponse(Material m) {
        MaterialResponseDTO dto = new MaterialResponseDTO();
        dto.setId(m.getIdMaterial());
        dto.setCodigoMaterial(m.getCodigoMaterial());
        dto.setNombreMaterial(m.getNombre());
        dto.setUnidadMedida(m.getUnidadMedida());
        dto.setDescripcion(m.getDescripcion());
        dto.setActivo(m.getActivo());
        return dto;
    }

    public static void applyRequest(MaterialRequestDTO req, Material m) {
        m.setCodigoMaterial(req.getCodigoMaterial());
        m.setNombre(req.getNombreMaterial());
        m.setUnidadMedida(req.getUnidadMedida());
        m.setDescripcion(req.getDescripcion());
        if (req.getActivo() != null) {
            m.setActivo(req.getActivo());
        }
    }
}
