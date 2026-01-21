package com.luis.recipes_web.controller;

import com.luis.recipes_web.dto.common.SuggestItemDTO;
import com.luis.recipes_web.dto.recipe.RecipeRequestDTO;
import com.luis.recipes_web.dto.recipe.RecipeResponseDTO;
import com.luis.recipes_web.dto.recipematerial.RecipeMaterialResponseDTO;
import com.luis.recipes_web.service.RecipeMaterialService;
import com.luis.recipes_web.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final RecipeMaterialService recipeMaterialService;

    public RecipeController(
            RecipeService recipeService,
            RecipeMaterialService recipeMaterialService
    ) {
        this.recipeService = recipeService;
        this.recipeMaterialService = recipeMaterialService;
    }

    // GET - búsqueda paginada (q + activa)
    // Ej: /api/recipes?activa=true&q=304&page=0&size=20
    @GetMapping
    public ResponseEntity<Page<RecipeResponseDTO>> search(
            @RequestParam(required = false) Boolean activa,
            @RequestParam(required = false) String q,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(recipeService.search(activa, q, pageable));
    }

    // GET - autocompletado incremental (suggest)
    @GetMapping("/suggest")
    public ResponseEntity<List<SuggestItemDTO>> suggest(
            @RequestParam(required = false) Boolean activa,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer limit
    ) {
        return ResponseEntity.ok(recipeService.suggest(activa, q, limit));
    }

    // GET - obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponseDTO> getById(@PathVariable("id") Long idRecipe) {
        return ResponseEntity.ok(recipeService.findById(idRecipe));
    }

    // GET - listar materiales de una receta
    @GetMapping("/{idRecipe}/materials")
    public ResponseEntity<List<RecipeMaterialResponseDTO>> listMaterialsByRecipe(
            @PathVariable("idRecipe") Long idRecipe
    ) {
        return ResponseEntity.ok(recipeMaterialService.listMaterialsByRecipe(idRecipe));
    }

    // POST - crear
    @PostMapping
    public ResponseEntity<RecipeResponseDTO> create(@Valid @RequestBody RecipeRequestDTO request) {
        RecipeResponseDTO created = recipeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT - actualizar
    @PutMapping("/{id}")
    public ResponseEntity<RecipeResponseDTO> update(
            @PathVariable("id") Long idRecipe,
            @Valid @RequestBody RecipeRequestDTO request
    ) {
        return ResponseEntity.ok(recipeService.update(idRecipe, request));
    }

    // DELETE - borrado lógico (activa=false)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable("id") Long idRecipe) {
        recipeService.deactivate(idRecipe);
        return ResponseEntity.noContent().build();
    }

    // DELETE - borrado físico (uso excepcional)
    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDelete(@PathVariable("id") Long idRecipe) {
        recipeService.delete(idRecipe);
        return ResponseEntity.noContent().build();
    }
}
