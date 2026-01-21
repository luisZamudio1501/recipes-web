package com.luis.recipes_web.service.impl;

import com.luis.recipes_web.dominio.Material;
import com.luis.recipes_web.dto.common.SuggestItemDTO;
import com.luis.recipes_web.dto.material.MaterialRequestDTO;
import com.luis.recipes_web.dto.material.MaterialResponseDTO;
import com.luis.recipes_web.exception.DuplicateException;
import com.luis.recipes_web.exception.NotFoundException;
import com.luis.recipes_web.mapper.MaterialMapper;
import com.luis.recipes_web.repositorio.MaterialRepository;
import com.luis.recipes_web.service.MaterialService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;

    public MaterialServiceImpl(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    // =================
    // CRUD
    // =================

    @Override
    @Transactional(readOnly = true)
    public List<MaterialResponseDTO> findAll() {
        return materialRepository.findAll()
                .stream()
                .map(MaterialMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MaterialResponseDTO findById(Long id) {
        Material m = materialRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Material no encontrado: id=" + id));
        return MaterialMapper.toResponse(m);
    }

    @Override
    public MaterialResponseDTO create(MaterialRequestDTO request) {

        // Normalización mínima antes de mapear
        request.setCodigoMaterial(safeTrim(request.getCodigoMaterial()));
        request.setNombreMaterial(safeTrim(request.getNombreMaterial()));

        String codigo = request.getCodigoMaterial();
        if (codigo == null) {
            throw new IllegalArgumentException("codigoMaterial es obligatorio");
        }

        if (materialRepository.existsByCodigoMaterial(codigo)) {
            throw new DuplicateException("Ya existe un material con codigoMaterial=" + codigo);
        }

        // default (aunque hoy tu DTO lo exige con @NotNull, lo dejamos defensivo)
        if (request.getActivo() == null) {
            request.setActivo(true);
        }

        Material m = new Material();
        MaterialMapper.applyRequest(request, m);

        Material saved = materialRepository.save(m);
        return MaterialMapper.toResponse(saved);
    }

    @Override
    public MaterialResponseDTO update(Long id, MaterialRequestDTO request) {

        Material m = materialRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Material no encontrado: id=" + id));

        request.setCodigoMaterial(safeTrim(request.getCodigoMaterial()));
        request.setNombreMaterial(safeTrim(request.getNombreMaterial()));

        String nuevoCodigo = request.getCodigoMaterial();
        String codigoActual = safeTrim(m.getCodigoMaterial());

        if (nuevoCodigo != null && !nuevoCodigo.equals(codigoActual)) {
            if (materialRepository.existsByCodigoMaterial(nuevoCodigo)) {
                throw new DuplicateException("Ya existe un material con codigoMaterial=" + nuevoCodigo);
            }
        }

        MaterialMapper.applyRequest(request, m);

        Material saved = materialRepository.save(m);
        return MaterialMapper.toResponse(saved);
    }

    /**
     * SOFT delete: uso normal del sistema (marca activo=false)
     */
    @Override
    public void delete(Long id) {
        Material m = materialRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Material no encontrado: id=" + id));

        if (Boolean.FALSE.equals(m.getActivo())) {
            return; // ya estaba inactivo
        }

        m.setActivo(false);
        materialRepository.save(m);
    }

    /**
     * HARD delete: uso excepcional (borra físicamente)
     */
    @Override
    public void hardDelete(Long id) {
        if (!materialRepository.existsById(id)) {
            throw new NotFoundException("Material no encontrado: id=" + id);
        }
        materialRepository.deleteById(id);
    }

    // =========================
    // HITO 7: SEARCH + SUGGEST
    // =========================

    @Override
    @Transactional(readOnly = true)
    public Page<MaterialResponseDTO> search(Boolean activo, String q, Pageable pageable) {

        if (pageable != null && pageable.getPageSize() > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("pageSize máximo permitido: " + MAX_PAGE_SIZE);
        }

        String nq = normalize(q);

        int page = (pageable == null) ? 0 : Math.max(pageable.getPageNumber(), 0);
        int size = (pageable == null) ? 20 : Math.max(pageable.getPageSize(), 1);

        Sort defaultSort = Sort.by("codigoMaterial").ascending()
                .and(Sort.by("nombre").ascending());

        Sort sortToUse = (pageable == null || pageable.getSort().isUnsorted())
                ? defaultSort
                : pageable.getSort();

        Pageable fixed = PageRequest.of(page, size, sortToUse);

        return materialRepository.search(activo, nq, fixed)
                .map(MaterialMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SuggestItemDTO> suggest(Boolean activo, String q, Integer limit) {

        String nq = normalize(q);
        if (nq == null) {
            return List.of(); // evita "traer todo"
        }

        int lim;
        if (limit == null) {
            lim = MAX_SUGGEST_LIMIT;
        } else if (limit < 1) {
            return List.of();
        } else {
            lim = Math.min(limit, MAX_SUGGEST_LIMIT);
        }

        Pageable topN = PageRequest.of(
                0,
                lim,
                Sort.by("codigoMaterial").ascending()
                        .and(Sort.by("nombre").ascending())
        );

        return materialRepository.suggest(activo, nq, topN).stream()
                .map(m -> {
                    String codigo = safeTrim(m.getCodigoMaterial());
                    String nombre = safeTrim(m.getNombre());
                    String label = (codigo == null ? "" : codigo) + " - " + (nombre == null ? "" : nombre);
                    return new SuggestItemDTO(m.getIdMaterial(), label, codigo);
                })
                .toList();
    }

    // =================
    // Helpers
    // =================

    private String normalize(String q) {
        return safeTrim(q);
    }

    private String safeTrim(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
