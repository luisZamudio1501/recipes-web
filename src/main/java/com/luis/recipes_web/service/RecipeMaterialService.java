package com.luis.recipes_web.service;

import com.luis.recipes_web.dto.recipematerial.RecipeMaterialRequestDTO;
import com.luis.recipes_web.dto.recipematerial.RecipeMaterialResponseDTO;

import java.util.List;

public interface RecipeMaterialService {

    RecipeMaterialResponseDTO addMaterialToRecipe(RecipeMaterialRequestDTO request);

    List<RecipeMaterialResponseDTO> listMaterialsByRecipe(Long idRecipe);

    void deleteRecipeMaterial(Long idRecipeMaterial);
}
