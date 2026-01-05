package com.luis.recipes_web.controller;

import com.luis.recipes_web.dominio.Recipe;
import com.luis.recipes_web.dominio.RecipeTask;
import com.luis.recipes_web.exception.NotFoundException;
import com.luis.recipes_web.service.RecipeTaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RecipeTaskController {

    private final RecipeTaskService recipeTaskService;

    public RecipeTaskController(RecipeTaskService recipeTaskService) {
        this.recipeTaskService = recipeTaskService;
    }

    // =========================================================
    // TAREAS POR RECETA
    // =========================================================

    // GET /api/recipes/{idRecipe}/tasks
    @GetMapping("/recipes/{idRecipe}/tasks")
    public ResponseEntity<List<RecipeTask>> getTasksByRecipe(@PathVariable Long idRecipe) {
        List<RecipeTask> tasks = recipeTaskService.findByRecipeId(idRecipe);
        return ResponseEntity.ok(tasks);
    }

    // POST /api/recipes/{idRecipe}/tasks
    @PostMapping("/recipes/{idRecipe}/tasks")
    public ResponseEntity<RecipeTask> createTaskForRecipe(
            @PathVariable Long idRecipe,
            @Valid @RequestBody RecipeTask recipeTask
    ) {
        // Fuerzo que la task apunte a la recipe del path (no confío en lo que venga en el body)
        Recipe recipeRef = new Recipe();
        recipeRef.setIdRecipe(idRecipe);
        recipeTask.setRecipe(recipeRef);

        // usa el método específico para crear task bajo recipe (y valida existencia)
        RecipeTask created = recipeTaskService.createForRecipe(idRecipe, recipeTask);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // GET /api/recipes/{idRecipe}/tasks/{idRecipeTask}
    @GetMapping("/recipes/{idRecipe}/tasks/{idRecipeTask}")
    public ResponseEntity<RecipeTask> getTaskForRecipe(
            @PathVariable Long idRecipe,
            @PathVariable Long idRecipeTask
    ) {
        RecipeTask task = recipeTaskService.getForRecipe(idRecipe, idRecipeTask);
        return ResponseEntity.ok(task);
    }

    // PUT /api/recipes/{idRecipe}/tasks/{idRecipeTask}
    @PutMapping("/recipes/{idRecipe}/tasks/{idRecipeTask}")
    public ResponseEntity<RecipeTask> updateTaskForRecipe(
            @PathVariable Long idRecipe,
            @PathVariable Long idRecipeTask,
            @Valid @RequestBody RecipeTask recipeTask
    ) {
        RecipeTask updated = recipeTaskService.updateForRecipe(idRecipe, idRecipeTask, recipeTask);
        return ResponseEntity.ok(updated);
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

    // =========================================================
    // CRUD POR ID DE TASK (GLOBAL)
    // =========================================================

    // GET /api/recipe-tasks/{id}
    @GetMapping("/recipe-tasks/{id}")
    public ResponseEntity<RecipeTask> getById(@PathVariable Long id) {
        RecipeTask task = recipeTaskService.findById(id)
                .orElseThrow(() -> new NotFoundException("RecipeTask no encontrada: id=" + id));
        return ResponseEntity.ok(task);
    }

    // PUT /api/recipe-tasks/{id}
    @PutMapping("/recipe-tasks/{id}")
    public ResponseEntity<RecipeTask> update(
            @PathVariable Long id,
            @Valid @RequestBody RecipeTask recipeTask
    ) {
        RecipeTask updated = recipeTaskService.update(id, recipeTask);
        return ResponseEntity.ok(updated);
    }

    // DELETE /api/recipe-tasks/{id}
    @DeleteMapping("/recipe-tasks/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        recipeTaskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/recipe-tasks
    @GetMapping("/recipe-tasks")
    public ResponseEntity<List<RecipeTask>> getAll() {
        return ResponseEntity.ok(recipeTaskService.findAll());
    }
}
