package com.luis.recipes_web.controller;

import com.luis.recipes_web.dominio.Recipe;
import com.luis.recipes_web.dominio.RecipeTask;
import com.luis.recipes_web.dto.recipetask.RecipeTaskRequestDTO;
import com.luis.recipes_web.dto.recipetask.RecipeTaskResponseDTO;
import com.luis.recipes_web.exception.NotFoundException;
import com.luis.recipes_web.mapper.RecipeTaskMapper;
import com.luis.recipes_web.service.RecipeTaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class RecipeTaskController {

    private final RecipeTaskService recipeTaskService;

    public RecipeTaskController(RecipeTaskService recipeTaskService) {
        this.recipeTaskService = recipeTaskService;
    }

    // GET /api/recipes/{idRecipe}/tasks
    @GetMapping("/recipes/{idRecipe}/tasks")
    public ResponseEntity<List<RecipeTaskResponseDTO>> getTasksByRecipe(@PathVariable Long idRecipe) {

        List<RecipeTaskResponseDTO> result = recipeTaskService.findByRecipeId(idRecipe)
                .stream()
                .map(RecipeTaskMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    // POST /api/recipes/{idRecipe}/tasks
    @PostMapping("/recipes/{idRecipe}/tasks")
    public ResponseEntity<RecipeTaskResponseDTO> createTaskForRecipe(
            @PathVariable Long idRecipe,
            @Valid @RequestBody RecipeTaskRequestDTO request
    ) {
        Recipe recipeRef = new Recipe();
        recipeRef.setIdRecipe(idRecipe);

        RecipeTask entity = RecipeTaskMapper.toEntity(request, recipeRef);
        RecipeTask created = recipeTaskService.createForRecipe(idRecipe, entity);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RecipeTaskMapper.toResponse(created));
    }

    // GET /api/recipes/{idRecipe}/tasks/{idRecipeTask}
    @GetMapping("/recipes/{idRecipe}/tasks/{idRecipeTask}")
    public ResponseEntity<RecipeTaskResponseDTO> getTaskForRecipe(
            @PathVariable Long idRecipe,
            @PathVariable Long idRecipeTask
    ) {
        RecipeTask task = recipeTaskService.getForRecipe(idRecipe, idRecipeTask);
        return ResponseEntity.ok(RecipeTaskMapper.toResponse(task));
    }

    // PUT /api/recipes/{idRecipe}/tasks/{idRecipeTask}
    @PutMapping("/recipes/{idRecipe}/tasks/{idRecipeTask}")
    public ResponseEntity<RecipeTaskResponseDTO> updateTaskForRecipe(
            @PathVariable Long idRecipe,
            @PathVariable Long idRecipeTask,
            @Valid @RequestBody RecipeTaskRequestDTO request
    ) {
        RecipeTask patch = new RecipeTask();
        RecipeTaskMapper.updateEntity(patch, request);

        RecipeTask updated = recipeTaskService.updateForRecipe(idRecipe, idRecipeTask, patch);

        return ResponseEntity.ok(RecipeTaskMapper.toResponse(updated));
    }

    // DELETE /api/recipes/{idRecipe}/tasks/{idRecipeTask}
    @DeleteMapping("/recipes/{idRecipe}/tasks/{idRecipeTask}")
    public ResponseEntity<Void> deleteTaskForRecipe(
            @PathVariable Long idRecipe,
            @PathVariable Long idRecipeTask
    ) {
        recipeTaskService.deleteForRecipe(idRecipe, idRecipeTask);
        return ResponseEntity.noContent().build();
    }

    // GET /api/recipe-tasks/{id}
    @GetMapping("/recipe-tasks/{id}")
    public ResponseEntity<RecipeTaskResponseDTO> getById(@PathVariable Long id) {

        RecipeTask task = recipeTaskService.findById(id)
                .orElseThrow(() -> new NotFoundException("RecipeTask no encontrada: id=" + id));

        return ResponseEntity.ok(RecipeTaskMapper.toResponse(task));
    }

    // PUT /api/recipe-tasks/{id}
    @PutMapping("/recipe-tasks/{id}")
    public ResponseEntity<RecipeTaskResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody RecipeTaskRequestDTO request
    ) {
        RecipeTask patch = new RecipeTask();
        RecipeTaskMapper.updateEntity(patch, request);

        RecipeTask updated = recipeTaskService.update(id, patch);

        return ResponseEntity.ok(RecipeTaskMapper.toResponse(updated));
    }

    // DELETE /api/recipe-tasks/{id}
    @DeleteMapping("/recipe-tasks/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        recipeTaskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/recipe-tasks
    @GetMapping("/recipe-tasks")
    public ResponseEntity<List<RecipeTaskResponseDTO>> getAll() {

        List<RecipeTaskResponseDTO> result = recipeTaskService.findAll()
                .stream()
                .map(RecipeTaskMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}
