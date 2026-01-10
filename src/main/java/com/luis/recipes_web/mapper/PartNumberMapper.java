package com.luis.recipes_web.mapper;

import com.luis.recipes_web.dominio.PartNumber;
import com.luis.recipes_web.dto.partnumber.PartNumberRequestDTO;
import com.luis.recipes_web.dto.partnumber.PartNumberResponseDTO;

public class PartNumberMapper {

    public static PartNumber toEntity(PartNumberRequestDTO dto) {
        PartNumber entity = new PartNumber();
        entity.setCodigoPartNumber(dto.getCodigoPartNumber());
        entity.setNombrePartNumber(dto.getNombrePartNumber());
        entity.setActivo(dto.getActivo());
        return entity;
    }

    public static PartNumberResponseDTO toResponse(PartNumber entity) {
        PartNumberResponseDTO dto = new PartNumberResponseDTO();
        dto.setIdPart(entity.getIdPart());
        dto.setCodigoPartNumber(entity.getCodigoPartNumber());
        dto.setNombrePartNumber(entity.getNombrePartNumber());
        dto.setActivo(entity.getActivo());
        return dto;
    }
}
