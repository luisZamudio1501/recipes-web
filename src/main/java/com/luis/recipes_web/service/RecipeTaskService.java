package com.luis.recipes_web.service;

import com.luis.recipes_web.dominio.RecipeTask;

import java.util.List;
import java.util.Optional;

public interface RecipeTaskService {

    // CRUD base
    RecipeTask create(RecipeTask recipeTask);

    RecipeTask update(Long idRecipeTask, RecipeTask recipeTask);

    void delete(Long idRecipeTask);

    Optional<RecipeTask> findById(Long idRecipeTask);

    List<RecipeTask> findAll();

    List<RecipeTask> findByRecipeId(Long idRecipe);

    // Para el endpoint: POST /api/recipes/{id}/tasks
    RecipeTask createForRecipe(Long idRecipe, RecipeTask recipeTask);

    RecipeTask getForRecipe(Long idRecipe, Long idRecipeTask);

    RecipeTask updateForRecipe(Long idRecipe, Long idRecipeTask, RecipeTask recipeTask);

    void deleteForRecipe(Long idRecipe, Long idRecipeTask);


}
