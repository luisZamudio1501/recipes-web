package com.luis.recipes_web.service;

import com.luis.recipes_web.dto.materialcost.MaterialCostRequestDTO;
import com.luis.recipes_web.dto.materialcost.MaterialCostResponseDTO;

import java.util.List;

public interface MaterialCostService {

    MaterialCostResponseDTO create(MaterialCostRequestDTO request);

    MaterialCostResponseDTO getCurrentByMaterial(Long idMaterial);

    List<MaterialCostResponseDTO> listByMaterial(Long idMaterial);

    void delete(Long idMaterialCost);

    MaterialCostResponseDTO update(Long idMaterialCost, MaterialCostRequestDTO request);


}
