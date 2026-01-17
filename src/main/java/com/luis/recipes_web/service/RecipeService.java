package com.luis.recipes_web.service;

import com.luis.recipes_web.dominio.Recipe;
import com.luis.recipes_web.dto.recipe.RecipeRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RecipeService {

    Recipe create(RecipeRequestDTO request);

    Recipe update(Long idRecipe, RecipeRequestDTO request);

    void delete(Long idRecipe);

    Optional<Recipe> findById(Long idRecipe);

    List<Recipe> findAll();

    // === HITO 7 ===
    Page<Recipe> search(Boolean activo, String q, Pageable pageable);

    List<SuggestItem> suggest(Boolean activo, String q, Integer limit);

    record SuggestItem(Long id, String label, String codigoPartNumber, Boolean activo) {}
}
