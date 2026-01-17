package com.luis.recipes_web.service.impl;

import com.luis.recipes_web.dominio.Material;
import com.luis.recipes_web.dominio.Recipe;
import com.luis.recipes_web.dominio.RecipeMaterial;
import com.luis.recipes_web.dto.recipematerial.RecipeMaterialRequestDTO;
import com.luis.recipes_web.dto.recipematerial.RecipeMaterialResponseDTO;
import com.luis.recipes_web.exception.DuplicateException;
import com.luis.recipes_web.exception.NotFoundException;
import com.luis.recipes_web.mapper.RecipeMaterialMapper;
import com.luis.recipes_web.repositorio.MaterialRepository;
import com.luis.recipes_web.repositorio.RecipeMaterialRepository;
import com.luis.recipes_web.repositorio.RecipeRepository;
import com.luis.recipes_web.service.RecipeMaterialService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecipeMaterialServiceImpl implements RecipeMaterialService {

    private final RecipeMaterialRepository recipeMaterialRepository;
    private final RecipeRepository recipeRepository;
    private final MaterialRepository materialRepository;

    public RecipeMaterialServiceImpl(
            RecipeMaterialRepository recipeMaterialRepository,
            RecipeRepository recipeRepository,
            MaterialRepository materialRepository
    ) {
        this.recipeMaterialRepository = recipeMaterialRepository;
        this.recipeRepository = recipeRepository;
        this.materialRepository = materialRepository;
    }

    @Override
    @Transactional
    public RecipeMaterialResponseDTO addMaterialToRecipe(RecipeMaterialRequestDTO request) {

        if (recipeMaterialRepository.existsByRecipe_IdRecipeAndMaterial_IdMaterial(
                request.getIdRecipe(), request.getIdMaterial())) {
            throw new DuplicateException("Ese material ya está cargado en la receta.");
        }

        Recipe recipe = recipeRepository.findById(request.getIdRecipe())
                .orElseThrow(() -> new NotFoundException("No existe la receta id=" + request.getIdRecipe()));

        Material material = materialRepository.findById(request.getIdMaterial())
                .orElseThrow(() -> new NotFoundException("No existe el material id=" + request.getIdMaterial()));

        RecipeMaterial rm = new RecipeMaterial();
        rm.setRecipe(recipe);
        rm.setMaterial(material);
        rm.setCantidadPorUnidad(request.getCantidadPorUnidad());
        rm.setMermaPorcentaje(request.getMermaPorcentaje());
        rm.setNota(request.getNota());

        RecipeMaterial saved = recipeMaterialRepository.save(rm);
        return RecipeMaterialMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecipeMaterialResponseDTO> listMaterialsByRecipe(Long idRecipe) {
        return recipeMaterialRepository.findByRecipeIdFetchMaterial(idRecipe)
                .stream()
                .map(RecipeMaterialMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public void deleteRecipeMaterial(Long idRecipeMaterial) {
        if (!recipeMaterialRepository.existsById(idRecipeMaterial)) {
            throw new NotFoundException("No existe recipe_material id=" + idRecipeMaterial);
        }
        recipeMaterialRepository.deleteById(idRecipeMaterial);
    }
}
