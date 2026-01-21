package com.luis.recipes_web.mapper;

import com.luis.recipes_web.dominio.PartNumber;
import com.luis.recipes_web.dto.partnumber.PartNumberRequestDTO;
import com.luis.recipes_web.dto.partnumber.PartNumberResponseDTO;
import com.luis.recipes_web.dominio.PartNumber;
import com.luis.recipes_web.dto.partnumber.PartNumberRequestDTO;


public final class PartNumberMapper {

    private PartNumberMapper() {
        // evita instanciación
    }

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

    public static void applyRequest(PartNumberRequestDTO request, PartNumber entity) {
        if (request == null || entity == null) return;

        // si querés permitir updates parciales, dejalo con if != null
        if (request.getCodigoPartNumber() != null) {
            entity.setCodigoPartNumber(request.getCodigoPartNumber().trim());
        }
        if (request.getNombrePartNumber() != null) {
            entity.setNombrePartNumber(request.getNombrePartNumber().trim());
        }
        if (request.getActivo() != null) {
            entity.setActivo(request.getActivo());
        }
    }

}
