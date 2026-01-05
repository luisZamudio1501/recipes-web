package com.luis.recipes_web.repositorio;

import com.luis.recipes_web.dominio.RecipeTask;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import java.util.List;

public interface RecipeTaskRepository extends JpaRepository<RecipeTask, Long> {

    // Recomendado: devuelve ordenadas por "orden"
    List<RecipeTask> findByRecipeIdRecipeOrderByOrdenAsc(Long idRecipe);

    // Opcional: sin ordenar (si lo necesitás)
    List<RecipeTask> findByRecipe_IdRecipe(Long idRecipe);

    Optional<RecipeTask> findByIdRecipeTaskAndRecipe_IdRecipe(Long idRecipeTask, Long idRecipe);

}
