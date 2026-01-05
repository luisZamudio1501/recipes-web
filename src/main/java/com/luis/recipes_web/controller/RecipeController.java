package com.luis.recipes_web.controller;

import com.luis.recipes_web.dominio.Recipe;
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
    public ResponseEntity<List<Recipe>> getAll() {
        return ResponseEntity.ok(recipeService.findAll());
    }

    // GET - obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getById(@PathVariable("id") Long idRecipe) {
        return recipeService.findById(idRecipe)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST - crear
    @PostMapping
    public ResponseEntity<Recipe> create(@RequestBody Recipe recipe) {
        Recipe created = recipeService.create(recipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT - actualizar
    @PutMapping("/{id}")
    public ResponseEntity<Recipe> update(
            @PathVariable("id") Long idRecipe,
            @RequestBody Recipe recipe
    ) {
        Recipe updated = recipeService.update(idRecipe, recipe);
        return ResponseEntity.ok(updated);
    }

    // DELETE - eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long idRecipe) {
        recipeService.delete(idRecipe);
        return ResponseEntity.noContent().build();
    }
}
