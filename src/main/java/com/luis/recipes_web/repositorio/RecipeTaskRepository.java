package com.luis.recipes_web.repositorio;

import com.luis.recipes_web.dominio.RecipeTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeTaskRepository extends JpaRepository<RecipeTask, Long> {

    // Devuelve ordenadas por "orden"
    List<RecipeTask> findByRecipe_IdRecipeOrderByOrdenAsc(Long idRecipe);

    // Opcional: sin ordenar
    List<RecipeTask> findByRecipe_IdRecipe(Long idRecipe);

    // Buscar una task dentro de una recipe
    Optional<RecipeTask> findByIdRecipeTaskAndRecipe_IdRecipe(Long idRecipeTask, Long idRecipe);

}
