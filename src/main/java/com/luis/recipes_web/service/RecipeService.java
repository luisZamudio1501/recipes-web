package com.luis.recipes_web.service;

import com.luis.recipes_web.dto.common.SuggestItemDTO;
import com.luis.recipes_web.dto.recipe.RecipeRequestDTO;
import com.luis.recipes_web.dto.recipe.RecipeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecipeService {

    List<RecipeResponseDTO> findAll();

    RecipeResponseDTO findById(Long idRecipe);

    RecipeResponseDTO create(RecipeRequestDTO request);

    RecipeResponseDTO update(Long idRecipe, RecipeRequestDTO request);

    // Soft delete (uso normal): activa=false
    void deactivate(Long idRecipe);

    // Hard delete (uso excepcional)
    void delete(Long idRecipe);

    Page<RecipeResponseDTO> search(Boolean activa, String q, Pageable pageable);

    List<SuggestItemDTO> suggest(Boolean activa, String q, Integer limit);
}
