package com.luis.recipes_web.service;

import com.luis.recipes_web.dominio.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeService {

    Recipe create(Recipe recipe);

    Recipe update(Long idRecipe, Recipe recipe);

    void delete(Long idRecipe);

    Optional<Recipe> findById(Long idRecipe);

    List<Recipe> findAll();

    // Si querés un método que devuelva Recipe directo (sin Optional), usá esto:
    // Recipe getById(Long idRecipe);
}
