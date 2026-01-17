package com.luis.recipes_web.mapper;

import com.luis.recipes_web.dominio.MaterialCost;
import com.luis.recipes_web.dto.materialcost.MaterialCostResponseDTO;

public final class MaterialCostMapper {

    private MaterialCostMapper() {
    }

    public static MaterialCostResponseDTO toResponseDTO(MaterialCost mc) {
        MaterialCostResponseDTO dto = new MaterialCostResponseDTO();

        dto.setIdMaterialCost(mc.getIdMaterialCost());

        if (mc.getMaterial() != null) {
            dto.setIdMaterial(mc.getMaterial().getIdMaterial());
            dto.setCodigoMaterial(mc.getMaterial().getCodigoMaterial());
            dto.setNombreMaterial(mc.getMaterial().getNombre());
        }

        dto.setCostoUnitario(mc.getCostoUnitario());
        dto.setMoneda(mc.getMoneda());
        dto.setVigenciaDesde(mc.getVigenciaDesde());
        dto.setVigenciaHasta(mc.getVigenciaHasta());
        dto.setProveedor(mc.getProveedor());
        dto.setObservacion(mc.getObservacion());
        dto.setCreatedAt(mc.getCreatedAt());
        dto.setUpdatedAt(mc.getUpdatedAt());

        return dto;
    }
}
