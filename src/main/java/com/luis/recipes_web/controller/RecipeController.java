package com.luis.recipes_web.controller;

import com.luis.recipes_web.dto.recipe.RecipeRequestDTO;
import com.luis.recipes_web.dto.recipe.RecipeResponseDTO;
import com.luis.recipes_web.mapper.RecipeMapper;
import com.luis.recipes_web.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    // GET - listar todas
    @GetMapping
    public ResponseEntity<List<RecipeResponseDTO>> getAll() {

        List<RecipeResponseDTO> result = recipeService.findAll()
                .stream()
                .map(RecipeMapper::toResponse)
                .toList();

        return ResponseEntity.ok(result);
    }

    // GET - obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponseDTO> getById(
            @PathVariable("id") Long idRecipe) {

        return recipeService.findById(idRecipe)
                .map(RecipeMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST - crear
    @PostMapping
    public ResponseEntity<RecipeResponseDTO> create(
            @RequestBody RecipeRequestDTO request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RecipeMapper.toResponse(recipeService.create(request)));
    }

    // PUT - actualizar
    @PutMapping("/{id}")
    public ResponseEntity<RecipeResponseDTO> update(
            @PathVariable("id") Long idRecipe,
            @RequestBody RecipeRequestDTO request
    ) {
        return ResponseEntity.ok(
                RecipeMapper.toResponse(recipeService.update(idRecipe, request))
        );
    }

    // DELETE - eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") Long idRecipe) {

        recipeService.delete(idRecipe);
        return ResponseEntity.noContent().build();
    }
}
