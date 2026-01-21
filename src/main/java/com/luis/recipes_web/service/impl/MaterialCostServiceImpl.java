package com.luis.recipes_web.service.impl;

import com.luis.recipes_web.dominio.Material;
import com.luis.recipes_web.dominio.MaterialCost;
import com.luis.recipes_web.dto.materialcost.MaterialCostRequestDTO;
import com.luis.recipes_web.dto.materialcost.MaterialCostResponseDTO;
import com.luis.recipes_web.exception.NotFoundException;
import com.luis.recipes_web.mapper.MaterialCostMapper;
import com.luis.recipes_web.repositorio.MaterialCostRepository;
import com.luis.recipes_web.repositorio.MaterialRepository;
import com.luis.recipes_web.service.MaterialCostService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MaterialCostServiceImpl implements MaterialCostService {

    private static final LocalDate MAX_DATE = LocalDate.of(9999, 12, 31);

    private final MaterialCostRepository materialCostRepository;
    private final MaterialRepository materialRepository;

    public MaterialCostServiceImpl(
            MaterialCostRepository materialCostRepository,
            MaterialRepository materialRepository
    ) {
        this.materialCostRepository = materialCostRepository;
        this.materialRepository = materialRepository;
    }

    @Override
    @Transactional
    public MaterialCostResponseDTO create(MaterialCostRequestDTO request) {

        Material material = materialRepository.findById(request.getIdMaterial())
                .orElseThrow(() -> new NotFoundException("No existe el material id=" + request.getIdMaterial()));

        validateRequest(request);

        LocalDate newDesde = request.getVigenciaDesde();
        LocalDate newHasta = (request.getVigenciaHasta() == null) ? MAX_DATE : request.getVigenciaHasta();

        long overlaps = materialCostRepository.countOverlapsNormalized(
                material.getIdMaterial(),
                newDesde,
                newHasta,
                MAX_DATE
        );

        if (overlaps > 0) {
            throw new IllegalArgumentException(
                    "Ya existe un costo que se cruza con el rango indicado para material id=" + material.getIdMaterial()
            );
        }

        MaterialCost mc = new MaterialCost();
        mc.setMaterial(material);
        applyRequestToEntity(request, mc);

        MaterialCost saved = materialCostRepository.save(mc);
        return MaterialCostMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public MaterialCostResponseDTO getCurrentByMaterial(Long idMaterial) {

        materialRepository.findById(idMaterial)
                .orElseThrow(() -> new NotFoundException("No existe el material id=" + idMaterial));

        MaterialCost current = materialCostRepository.findCurrentByMaterial(idMaterial, LocalDate.now())
                .orElseThrow(() -> new NotFoundException("No hay costo vigente para material id=" + idMaterial));

        return MaterialCostMapper.toResponseDTO(current);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaterialCostResponseDTO> listByMaterial(Long idMaterial) {

        materialRepository.findById(idMaterial)
                .orElseThrow(() -> new NotFoundException("No existe el material id=" + idMaterial));

        return materialCostRepository.findByMaterial_IdMaterialOrderByVigenciaDesdeDesc(idMaterial)
                .stream()
                .map(MaterialCostMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long idMaterialCost) {
        MaterialCost existing = materialCostRepository.findById(idMaterialCost)
                .orElseThrow(() -> new NotFoundException("No existe material_cost id=" + idMaterialCost));

        materialCostRepository.delete(existing);
    }

    @Override
    @Transactional
    public MaterialCostResponseDTO update(Long idMaterialCost, MaterialCostRequestDTO request) {

        MaterialCost existing = materialCostRepository.findById(idMaterialCost)
                .orElseThrow(() -> new NotFoundException("No existe material_cost id=" + idMaterialCost));

        // Regla: NO se permite cambiar el material del costo
        Long existingIdMaterial = existing.getMaterial() == null ? null : existing.getMaterial().getIdMaterial();
        if (existingIdMaterial == null) {
            throw new IllegalStateException("MaterialCost id=" + idMaterialCost + " no tiene material asociado");
        }
        if (request.getIdMaterial() == null || !existingIdMaterial.equals(request.getIdMaterial())) {
            throw new IllegalArgumentException("No se permite cambiar el material de un costo existente");
        }

        validateRequest(request);

        LocalDate newDesde = request.getVigenciaDesde();
        LocalDate newHasta = (request.getVigenciaHasta() == null) ? MAX_DATE : request.getVigenciaHasta();

        long overlaps = materialCostRepository.countOverlapsNormalizedExcludingId(
                existingIdMaterial,
                idMaterialCost,
                newDesde,
                newHasta,
                MAX_DATE
        );

        if (overlaps > 0) {
            throw new IllegalArgumentException(
                    "Ya existe un costo que se cruza con el rango indicado para material id=" + existingIdMaterial
            );
        }

        applyRequestToEntity(request, existing);

        MaterialCost saved = materialCostRepository.save(existing);
        return MaterialCostMapper.toResponseDTO(saved);
    }

    // =================
    // Helpers
    // =================

    private void validateRequest(MaterialCostRequestDTO request) {
        if (request.getCostoUnitario() == null || request.getCostoUnitario().signum() <= 0) {
            throw new IllegalArgumentException("costoUnitario debe ser mayor a 0");
        }
        if (request.getVigenciaDesde() == null) {
            throw new IllegalArgumentException("vigenciaDesde es obligatoria");
        }
        if (request.getVigenciaHasta() != null && request.getVigenciaHasta().isBefore(request.getVigenciaDesde())) {
            throw new IllegalArgumentException("vigenciaHasta no puede ser anterior a vigenciaDesde");
        }
    }

    private void applyRequestToEntity(MaterialCostRequestDTO request, MaterialCost target) {

        target.setCostoUnitario(request.getCostoUnitario());

        String moneda = normalizeUpper(request.getMoneda());
        target.setMoneda(moneda == null ? "ARS" : moneda);

        target.setVigenciaDesde(request.getVigenciaDesde());
        target.setVigenciaHasta(request.getVigenciaHasta());
        target.setProveedor(safeTrim(request.getProveedor()));
        target.setObservacion(safeTrim(request.getObservacion()));
    }

    private String safeTrim(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private String normalizeUpper(String s) {
        String t = safeTrim(s);
        return t == null ? null : t.toUpperCase();
    }
}
