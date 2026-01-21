package com.luis.recipes_web.service.impl;

import com.luis.recipes_web.dominio.PartNumber;
import com.luis.recipes_web.dto.common.SuggestItemDTO;
import com.luis.recipes_web.dto.partnumber.PartNumberRequestDTO;
import com.luis.recipes_web.dto.partnumber.PartNumberResponseDTO;
import com.luis.recipes_web.exception.DuplicateException;
import com.luis.recipes_web.exception.NotFoundException;
import com.luis.recipes_web.mapper.PartNumberMapper;
import com.luis.recipes_web.repositorio.PartNumberRepository;
import com.luis.recipes_web.service.PartNumberService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PartNumberServiceImpl implements PartNumberService {

    private final PartNumberRepository partNumberRepository;

    public PartNumberServiceImpl(PartNumberRepository partNumberRepository) {
        this.partNumberRepository = partNumberRepository;
    }

    // =================
    // CRUD (DTO)
    // =================

    @Override
    @Transactional(readOnly = true)
    public List<PartNumberResponseDTO> findAll() {
        return partNumberRepository.findAll()
                .stream()
                .map(PartNumberMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PartNumberResponseDTO findById(Long idPart) {
        PartNumber pn = partNumberRepository.findById(idPart)
                .orElseThrow(() -> new NotFoundException("PartNumber no encontrado: id=" + idPart));
        return PartNumberMapper.toResponse(pn);
    }

    @Override
    @Transactional(readOnly = true)
    public PartNumberResponseDTO findByCodigo(String codigoPartNumber) {
        String codigo = safeTrim(codigoPartNumber);
        if (codigo == null) {
            throw new IllegalArgumentException("codigoPartNumber es obligatorio");
        }

        PartNumber pn = partNumberRepository.findByCodigoPartNumber(codigo)
                .orElseThrow(() -> new NotFoundException("PartNumber no encontrado: codigo=" + codigo));

        return PartNumberMapper.toResponse(pn);
    }

    @Override
    public PartNumberResponseDTO create(PartNumberRequestDTO request) {
        String codigo = safeTrim(request.getCodigoPartNumber());
        if (codigo == null) {
            throw new IllegalArgumentException("codigoPartNumber es obligatorio");
        }

        if (partNumberRepository.existsByCodigoPartNumber(codigo)) {
            throw new DuplicateException("Ya existe un PartNumber con codigoPartNumber=" + codigo);
        }

        // default
        if (request.getActivo() == null) {
            request.setActivo(true);
        }

        PartNumber pn = new PartNumber();
        PartNumberMapper.applyRequest(request, pn);

        PartNumber saved = partNumberRepository.save(pn);
        return PartNumberMapper.toResponse(saved);
    }

    @Override
    public PartNumberResponseDTO update(Long idPart, PartNumberRequestDTO request) {
        PartNumber existing = partNumberRepository.findById(idPart)
                .orElseThrow(() -> new NotFoundException("PartNumber no encontrado: id=" + idPart));

        String nuevoCodigo = safeTrim(request.getCodigoPartNumber());
        if (nuevoCodigo != null && !nuevoCodigo.equals(existing.getCodigoPartNumber())) {
            if (partNumberRepository.existsByCodigoPartNumber(nuevoCodigo)) {
                throw new DuplicateException("Ya existe un PartNumber con codigoPartNumber=" + nuevoCodigo);
            }
        }

        PartNumberMapper.applyRequest(request, existing);

        PartNumber saved = partNumberRepository.save(existing);
        return PartNumberMapper.toResponse(saved);
    }

    /**
     * Soft delete: marca activo=false
     */
    @Override
    public void delete(Long idPart) {
        PartNumber existing = partNumberRepository.findById(idPart)
                .orElseThrow(() -> new NotFoundException("PartNumber no encontrado: id=" + idPart));

        if (Boolean.FALSE.equals(existing.getActivo())) {
            return;
        }

        existing.setActivo(false);
        partNumberRepository.save(existing);
    }

    /**
     * Hard delete: borra físicamente
     */
    @Override
    public void hardDelete(Long idPart) {
        if (!partNumberRepository.existsById(idPart)) {
            throw new NotFoundException("PartNumber no encontrado: id=" + idPart);
        }
        partNumberRepository.deleteById(idPart);
    }

    // =========================
    // HITO 7: SEARCH + SUGGEST
    // =========================

    @Override
    @Transactional(readOnly = true)
    public Page<PartNumberResponseDTO> search(Boolean activo, String q, Pageable pageable) {

        if (pageable != null && pageable.getPageSize() > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("pageSize máximo permitido: " + MAX_PAGE_SIZE);
        }

        String nq = normalize(q);

        int page = (pageable == null) ? 0 : Math.max(pageable.getPageNumber(), 0);
        int size = (pageable == null) ? 20 : Math.max(pageable.getPageSize(), 1);

        Sort defaultSort = Sort.by("codigoPartNumber").ascending()
                .and(Sort.by("nombrePartNumber").ascending());

        Sort sortToUse = (pageable == null || pageable.getSort().isUnsorted())
                ? defaultSort
                : pageable.getSort();

        Pageable fixed = PageRequest.of(page, size, sortToUse);

        return partNumberRepository.search(activo, nq, fixed)
                .map(PartNumberMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SuggestItemDTO> suggest(Boolean activo, String q, Integer limit) {

        String nq = normalize(q);
        if (nq == null) {
            return List.of();
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
                Sort.by("codigoPartNumber").ascending()
                        .and(Sort.by("nombrePartNumber").ascending())
        );

        return partNumberRepository.suggest(activo, nq, topN).stream()
                .map(pn -> new SuggestItemDTO(
                        pn.getIdPart(),
                        pn.getCodigoPartNumber() + " - " + pn.getNombrePartNumber(),
                        pn.getCodigoPartNumber()
                ))
                .toList();
    }

    // =================
    // Helpers
    // =================

    private String normalize(String q) {
        if (q == null) return null;
        String t = q.trim().replaceAll("\\s+", " ");
        return t.isEmpty() ? null : t;
    }

    private String safeTrim(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
