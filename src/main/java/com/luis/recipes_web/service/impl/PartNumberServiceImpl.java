package com.luis.recipes_web.service.impl;

import com.luis.recipes_web.dominio.PartNumber;
import com.luis.recipes_web.exception.DuplicateException;
import com.luis.recipes_web.exception.NotFoundException;
import com.luis.recipes_web.repositorio.PartNumberRepository;
import com.luis.recipes_web.service.PartNumberService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PartNumberServiceImpl implements PartNumberService {

    private static final int MAX_PAGE_SIZE = 50;
    private static final int MAX_SUGGEST_LIMIT = 10;

    private final PartNumberRepository partNumberRepository;

    public PartNumberServiceImpl(PartNumberRepository partNumberRepository) {
        this.partNumberRepository = partNumberRepository;
    }

    @Override
    public PartNumber create(PartNumber partNumber) {

        // Regla de negocio: código único
        String codigo = partNumber.getCodigoPartNumber();
        if (partNumberRepository.findByCodigoPartNumber(codigo).isPresent()) {
            throw new DuplicateException("Ya existe un PartNumber con codigoPartNumber: " + codigo);
        }

        // createdAt/updatedAt/activo se manejan con @PrePersist en la entidad
        return partNumberRepository.save(partNumber);
    }

    @Override
    public PartNumber update(Long idPart, PartNumber partNumber) {

        PartNumber existing = partNumberRepository.findById(idPart)
                .orElseThrow(() -> new NotFoundException("PartNumber no encontrado: " + idPart));

        // Regla de negocio: si cambia el código, debe seguir siendo único
        String nuevoCodigo = partNumber.getCodigoPartNumber();
        partNumberRepository.findByCodigoPartNumber(nuevoCodigo).ifPresent(found -> {
            if (!found.getIdPart().equals(idPart)) {
                throw new DuplicateException("Ya existe un PartNumber con codigoPartNumber: " + nuevoCodigo);
            }
        });

        existing.setCodigoPartNumber(partNumber.getCodigoPartNumber());
        existing.setNombrePartNumber(partNumber.getNombrePartNumber());
        existing.setActivo(partNumber.getActivo());

        return partNumberRepository.save(existing);
    }

    @Override
    public void delete(Long idPart) {
        PartNumber existing = partNumberRepository.findById(idPart)
                .orElseThrow(() -> new NotFoundException("PartNumber no encontrado: " + idPart));
        partNumberRepository.delete(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PartNumber> findById(Long idPart) {
        return partNumberRepository.findById(idPart);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PartNumber> findByCodigo(String codigoPartNumber) {
        return partNumberRepository.findByCodigoPartNumber(codigoPartNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PartNumber> findAll() {
        return partNumberRepository.findAll();
    }

    // =========================
    // HITO 7: SEARCH + SUGGEST
    // =========================

    @Override
    @Transactional(readOnly = true)
    public Page<PartNumber> search(Boolean activo, String q, Pageable pageable) {
        String nq = normalize(q);

        int requestedSize = pageable == null ? 20 : pageable.getPageSize();
        int safeSize = Math.min(Math.max(requestedSize, 1), MAX_PAGE_SIZE);

        int requestedPage = pageable == null ? 0 : pageable.getPageNumber();
        int safePage = Math.max(requestedPage, 0);

        Pageable fixed = PageRequest.of(
                safePage,
                safeSize,
                Sort.by("codigoPartNumber").ascending()
                        .and(Sort.by("nombrePartNumber").ascending())
        );

        return partNumberRepository.search(activo, nq, fixed);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SuggestItem> suggest(Boolean activo, String q, Integer limit) {
        String nq = normalize(q);

        int lim = (limit == null) ? MAX_SUGGEST_LIMIT : Math.min(Math.max(limit, 1), MAX_SUGGEST_LIMIT);

        Pageable topN = PageRequest.of(
                0,
                lim,
                Sort.by("codigoPartNumber").ascending()
                        .and(Sort.by("nombrePartNumber").ascending())
        );

        return partNumberRepository.suggest(activo, nq, topN).stream()
                .map(pn -> new SuggestItem(
                        pn.getIdPart(),
                        pn.getCodigoPartNumber() + " - " + pn.getNombrePartNumber(),
                        pn.getCodigoPartNumber(),
                        pn.getActivo()
                ))
                .toList();
    }

    private String normalize(String q) {
        if (q == null) return null;
        String t = q.trim().replaceAll("\\s+", " ");
        return t.isEmpty() ? null : t;
    }
}
