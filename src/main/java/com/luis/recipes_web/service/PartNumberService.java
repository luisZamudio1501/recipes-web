package com.luis.recipes_web.service;

import com.luis.recipes_web.dominio.PartNumber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PartNumberService {

    PartNumber create(PartNumber partNumber);

    PartNumber update(Long idPart, PartNumber partNumber);

    void delete(Long idPart);

    Optional<PartNumber> findById(Long idPart);

    Optional<PartNumber> findByCodigo(String codigoPartNumber);

    List<PartNumber> findAll();

    // === HITO 7 ===
    Page<PartNumber> search(Boolean activo, String q, Pageable pageable);

    List<SuggestItem> suggest(Boolean activo, String q, Integer limit);

    // DTO liviano para autocompletado
    record SuggestItem(Long id, String label, String codigo, Boolean activo) {}
}
