package com.luis.recipes_web.service;

import com.luis.recipes_web.dto.common.SuggestItemDTO;
import com.luis.recipes_web.dto.partnumber.PartNumberRequestDTO;
import com.luis.recipes_web.dto.partnumber.PartNumberResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PartNumberService {

    // Límites estándar (alineado al patrón del sistema)
    int MAX_PAGE_SIZE = 50;
    int MAX_SUGGEST_LIMIT = 10;

    // === CRUD ===

    List<PartNumberResponseDTO> findAll();

    PartNumberResponseDTO findById(Long idPart);

    PartNumberResponseDTO findByCodigo(String codigoPartNumber);

    PartNumberResponseDTO create(PartNumberRequestDTO request);

    PartNumberResponseDTO update(Long idPart, PartNumberRequestDTO request);

    /**
     * Soft delete (uso normal): marca activo=false
     */
    void delete(Long idPart);

    /**
     * Hard delete (uso excepcional): borra físicamente
     */
    void hardDelete(Long idPart);

    // === HITO 7 ===
    Page<PartNumberResponseDTO> search(Boolean activo, String q, Pageable pageable);

    // === HITO 8 (standard suggest) ===
    List<SuggestItemDTO> suggest(Boolean activo, String q, Integer limit);
}
