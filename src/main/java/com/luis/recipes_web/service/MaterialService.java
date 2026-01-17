package com.luis.recipes_web.service;

import com.luis.recipes_web.dto.material.MaterialRequestDTO;
import com.luis.recipes_web.dto.material.MaterialResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MaterialService {

    List<MaterialResponseDTO> findAll();

    MaterialResponseDTO findById(Long id);

    MaterialResponseDTO create(MaterialRequestDTO request);

    MaterialResponseDTO update(Long id, MaterialRequestDTO request);

    void delete(Long id);

    // === HITO 7 ===
    Page<MaterialResponseDTO> search(Boolean activo, String q, Pageable pageable);

    List<SuggestItem> suggest(Boolean activo, String q, Integer limit);

    // DTO liviano para autocompletado
    record SuggestItem(Long id, String label, String codigo, Boolean activo) {}
}
