package com.luis.recipes_web.service.impl;

import com.luis.recipes_web.dominio.Material;
import com.luis.recipes_web.dto.material.MaterialRequestDTO;
import com.luis.recipes_web.dto.material.MaterialResponseDTO;
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

    private static final int MAX_PAGE_SIZE = 50;
    private static final int MAX_SUGGEST_LIMIT = 10;

    private final MaterialRepository materialRepository;

    public MaterialServiceImpl(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

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
                .orElseThrow(() -> new IllegalArgumentException("Material no encontrado: id=" + id));
        return MaterialMapper.toResponse(m);
    }

    @Override
    public MaterialResponseDTO create(MaterialRequestDTO request) {
        String codigo = request.getCodigoMaterial();
        if (materialRepository.existsByCodigoMaterial(codigo)) {
            throw new IllegalArgumentException("Ya existe un material con codigoMaterial=" + codigo);
        }

        Material m = new Material();
        // defaults
        if (request.getActivo() == null) request.setActivo(true);

        MaterialMapper.applyRequest(request, m);
        Material saved = materialRepository.save(m);
        return MaterialMapper.toResponse(saved);
    }

    @Override
    public MaterialResponseDTO update(Long id, MaterialRequestDTO request) {
        Material m = materialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Material no encontrado: id=" + id));

        String nuevoCodigo = request.getCodigoMaterial();
        if (nuevoCodigo != null && !nuevoCodigo.equals(m.getCodigoMaterial())) {
            if (materialRepository.existsByCodigoMaterial(nuevoCodigo)) {
                throw new IllegalArgumentException("Ya existe un material con codigoMaterial=" + nuevoCodigo);
            }
        }

        MaterialMapper.applyRequest(request, m);
        Material saved = materialRepository.save(m);
        return MaterialMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        if (!materialRepository.existsById(id)) {
            throw new IllegalArgumentException("Material no encontrado: id=" + id);
        }
        materialRepository.deleteById(id);
    }

    // =========================
    // HITO 7: SEARCH + SUGGEST
    // =========================

    @Override
    @Transactional(readOnly = true)
    public Page<MaterialResponseDTO> search(Boolean activo, String q, Pageable pageable) {
        String nq = normalize(q);

        int requestedSize = pageable == null ? 20 : pageable.getPageSize();
        int safeSize = Math.min(Math.max(requestedSize, 1), MAX_PAGE_SIZE);

        int requestedPage = pageable == null ? 0 : pageable.getPageNumber();
        int safePage = Math.max(requestedPage, 0);

        Pageable fixed = PageRequest.of(
                safePage,
                safeSize,
                Sort.by("codigoMaterial").ascending().and(Sort.by("nombre").ascending())
        );

        return materialRepository.search(activo, nq, fixed)
                .map(MaterialMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SuggestItem> suggest(Boolean activo, String q, Integer limit) {
        String nq = normalize(q);

        int lim = (limit == null) ? MAX_SUGGEST_LIMIT : Math.min(Math.max(limit, 1), MAX_SUGGEST_LIMIT);

        Pageable topN = PageRequest.of(
                0,
                lim,
                Sort.by("codigoMaterial").ascending().and(Sort.by("nombre").ascending())
        );

        return materialRepository.suggest(activo, nq, topN).stream()
                .map(m -> new SuggestItem(
                        m.getIdMaterial(),
                        m.getCodigoMaterial() + " - " + m.getNombre(),
                        m.getCodigoMaterial(),
                        m.getActivo()
                ))
                .toList();
    }

    private String normalize(String q) {
        if (q == null) return null;
        String t = q.trim().replaceAll("\\s+", " ");
        return t.isEmpty() ? null : t;
    }
}
