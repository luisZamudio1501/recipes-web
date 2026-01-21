package com.luis.recipes_web.service;

import com.luis.recipes_web.dto.common.SuggestItemDTO;
import com.luis.recipes_web.dto.material.MaterialRequestDTO;
import com.luis.recipes_web.dto.material.MaterialResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MaterialService {

    // Límites estándar (alineado al patrón del sistema)
    int MAX_PAGE_SIZE = 50;
    int MAX_SUGGEST_LIMIT = 10;

    // === CRUD ===

    List<MaterialResponseDTO> findAll();

    MaterialResponseDTO findById(Long id);

    MaterialResponseDTO create(MaterialRequestDTO request);

    MaterialResponseDTO update(Long id, MaterialRequestDTO request);

    /**
     * Soft delete (uso normal): marca activo=false.
     */
    void delete(Long id);

    /**
     * Hard delete (uso excepcional): borra físicamente.
     */
    void hardDelete(Long id);

    // === Búsqueda paginada ===

    /**
     * Búsqueda paginada por estado y texto.
     *
     * Reglas esperadas en la implementación:
     * - Si pageable.getPageSize() > MAX_PAGE_SIZE => error (IllegalArgumentException o tu excepción estándar)
     * - Si q es null/vacío => se ignora el filtro de texto
     * - activo null => no filtra por activo
     */
    Page<MaterialResponseDTO> search(Boolean activo, String q, Pageable pageable);

    // === Autocompletado / suggest ===

    /**
     * Devuelve resultados livianos para autocompletado.
     *
     * Reglas esperadas en la implementación:
     * - q null/vacío => lista vacía (para evitar traer todo)
     * - limit null => MAX_SUGGEST_LIMIT
     * - limit < 1 => lista vacía
     * - limit > MAX_SUGGEST_LIMIT => MAX_SUGGEST_LIMIT
     * - activo null => no filtra por activo
     */
    List<SuggestItemDTO> suggest(Boolean activo, String q, Integer limit);
}
