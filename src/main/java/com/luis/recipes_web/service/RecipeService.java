package com.luis.recipes_web.service;

import com.luis.recipes_web.dominio.Recipe;
import com.luis.recipes_web.dto.recipe.RecipeRequestDTO;

import java.util.List;
import java.util.Optional;

public interface RecipeService {

    Recipe create(RecipeRequestDTO request);

    Recipe update(Long idRecipe, RecipeRequestDTO request);

    void delete(Long idRecipe);

    Optional<Recipe> findById(Long idRecipe);

    List<Recipe> findAll();
}
