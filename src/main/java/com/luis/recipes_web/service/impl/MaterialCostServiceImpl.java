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

        if (request.getCostoUnitario() == null || request.getCostoUnitario().signum() <= 0) {
            throw new IllegalArgumentException("costoUnitario debe ser mayor a 0");
        }

        if (request.getVigenciaDesde() == null) {
            throw new IllegalArgumentException("vigenciaDesde es obligatoria");
        }

        if (request.getVigenciaHasta() != null && request.getVigenciaHasta().isBefore(request.getVigenciaDesde())) {
            throw new IllegalArgumentException("vigenciaHasta no puede ser anterior a vigenciaDesde");
        }

        // Anti-solapamiento: normalizamos "hasta" (NULL => infinito)
        LocalDate newDesde = request.getVigenciaDesde();
        LocalDate maxDate = LocalDate.of(9999, 12, 31);
        LocalDate newHasta = (request.getVigenciaHasta() == null) ? maxDate : request.getVigenciaHasta();

        long overlaps = materialCostRepository.countOverlapsNormalized(
                material.getIdMaterial(),
                newDesde,
                newHasta,
                maxDate
        );

        if (overlaps > 0) {
            throw new IllegalArgumentException(
                    "Ya existe un costo que se cruza con el rango indicado para material id=" + material.getIdMaterial()
            );
        }

        MaterialCost mc = new MaterialCost();
        mc.setMaterial(material);
        mc.setCostoUnitario(request.getCostoUnitario());

        String moneda = (request.getMoneda() == null || request.getMoneda().isBlank())
                ? "ARS"
                : request.getMoneda().trim().toUpperCase();

        mc.setMoneda(moneda);

        mc.setVigenciaDesde(request.getVigenciaDesde());
        mc.setVigenciaHasta(request.getVigenciaHasta());
        mc.setProveedor(request.getProveedor());
        mc.setObservacion(request.getObservacion());

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
}
