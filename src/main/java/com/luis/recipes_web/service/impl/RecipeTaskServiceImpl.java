package com.luis.recipes_web.service.impl;

import com.luis.recipes_web.dominio.Recipe;
import com.luis.recipes_web.dominio.RecipeTask;
import com.luis.recipes_web.exception.NotFoundException;
import com.luis.recipes_web.repositorio.RecipeRepository;
import com.luis.recipes_web.repositorio.RecipeTaskRepository;
import com.luis.recipes_web.service.RecipeTaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RecipeTaskServiceImpl implements RecipeTaskService {

    private final RecipeTaskRepository recipeTaskRepository;
    private final RecipeRepository recipeRepository;

    public RecipeTaskServiceImpl(RecipeTaskRepository recipeTaskRepository,
                                 RecipeRepository recipeRepository) {
        this.recipeTaskRepository = recipeTaskRepository;
        this.recipeRepository = recipeRepository;
    }

    // =======================
    // CREATE
    // =======================

    @Override
    public RecipeTask create(RecipeTask recipeTask) {
        validarRecipeObligatoria(recipeTask);
        return recipeTaskRepository.save(recipeTask);
    }

    @Override
    public RecipeTask createForRecipe(Long idRecipe, RecipeTask recipeTask) {
        if (recipeTask == null) {
            throw new IllegalArgumentException("RecipeTask es null");
        }
        if (idRecipe == null) {
            throw new IllegalArgumentException("idRecipe es null");
        }

        Recipe recipe = recipeRepository.findById(idRecipe)
                .orElseThrow(() -> new NotFoundException("Recipe no encontrada: id=" + idRecipe));

        recipeTask.setRecipe(recipe);
        return recipeTaskRepository.save(recipeTask);
    }

    // =======================
    // UPDATE (GLOBAL)
    // =======================

    @Override
    public RecipeTask update(Long idRecipeTask, RecipeTask recipeTask) {
        if (idRecipeTask == null) {
            throw new IllegalArgumentException("idRecipeTask es null");
        }

        RecipeTask existente = recipeTaskRepository.findById(idRecipeTask)
                .orElseThrow(() -> new NotFoundException("RecipeTask no encontrada: id=" + idRecipeTask));

        copiarCamposEditables(recipeTask, existente);
        validarRecipeObligatoria(existente);

        return recipeTaskRepository.save(existente);
    }

    // =======================
    // UPDATE (POR RECETA)
    // =======================

    @Override
    public RecipeTask updateForRecipe(Long idRecipe, Long idRecipeTask, RecipeTask recipeTask) {
        if (idRecipe == null) {
            throw new IllegalArgumentException("idRecipe es null");
        }
        if (idRecipeTask == null) {
            throw new IllegalArgumentException("idRecipeTask es null");
        }
        if (recipeTask == null) {
            throw new IllegalArgumentException("RecipeTask es null");
        }

        Recipe recipe = recipeRepository.findById(idRecipe)
                .orElseThrow(() -> new NotFoundException("Recipe no encontrada: id=" + idRecipe));

        RecipeTask existente = buscarTaskDeRecipe(idRecipe, idRecipeTask);

        // no permitir cambiar recipe desde el body
        copiarCamposEditablesSinRecipe(recipeTask, existente);
        existente.setRecipe(recipe);

        return recipeTaskRepository.save(existente);
    }

    // =======================
    // DELETE (GLOBAL)
    // =======================

    @Override
    public void delete(Long idRecipeTask) {
        if (idRecipeTask == null) {
            throw new IllegalArgumentException("idRecipeTask es null");
        }

        RecipeTask existente = recipeTaskRepository.findById(idRecipeTask)
                .orElseThrow(() -> new NotFoundException("RecipeTask no encontrada: id=" + idRecipeTask));

        recipeTaskRepository.delete(existente);
    }

    // =======================
    // DELETE (POR RECETA)
    // =======================

    @Override
    public void deleteForRecipe(Long idRecipe, Long idRecipeTask) {
        if (idRecipe == null) {
            throw new IllegalArgumentException("idRecipe es null");
        }
        if (idRecipeTask == null) {
            throw new IllegalArgumentException("idRecipeTask es null");
        }

        if (!recipeRepository.existsById(idRecipe)) {
            throw new NotFoundException("Recipe no encontrada: id=" + idRecipe);
        }

        RecipeTask existente = buscarTaskDeRecipe(idRecipe, idRecipeTask);
        recipeTaskRepository.delete(existente);
    }

    // =======================
    // READ (GLOBAL)
    // =======================

    @Override
    @Transactional(readOnly = true)
    public Optional<RecipeTask> findById(Long idRecipeTask) {
        return recipeTaskRepository.findById(idRecipeTask);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecipeTask> findAll() {
        return recipeTaskRepository.findAll();
    }

    // =======================
    // READ (POR RECETA)
    // =======================

    @Override
    @Transactional(readOnly = true)
    public List<RecipeTask> findByRecipeId(Long idRecipe) {
        if (idRecipe == null) {
            throw new IllegalArgumentException("idRecipe es null");
        }

        // ✅ NUEVO: validar que la recipe exista
        if (!recipeRepository.existsById(idRecipe)) {
            throw new NotFoundException("Recipe no encontrada: id=" + idRecipe);
        }

        return recipeTaskRepository.findByRecipeIdRecipeOrderByOrdenAsc(idRecipe);
    }

    @Override
    @Transactional(readOnly = true)
    public RecipeTask getForRecipe(Long idRecipe, Long idRecipeTask) {
        if (idRecipe == null) {
            throw new IllegalArgumentException("idRecipe es null");
        }
        if (idRecipeTask == null) {
            throw new IllegalArgumentException("idRecipeTask es null");
        }

        if (!recipeRepository.existsById(idRecipe)) {
            throw new NotFoundException("Recipe no encontrada: id=" + idRecipe);
        }

        return buscarTaskDeRecipe(idRecipe, idRecipeTask);
    }

    // =======================
    // Privados
    // =======================

    private void validarRecipeObligatoria(RecipeTask rt) {
        if (rt == null) {
            throw new IllegalArgumentException("RecipeTask es null");
        }

        Recipe r = rt.getRecipe();
        if (r == null || r.getIdRecipe() == null) {
            throw new IllegalArgumentException("RecipeTask debe tener Recipe (id_recipe) válido");
        }

        boolean existe = recipeRepository.existsById(r.getIdRecipe());
        if (!existe) {
            throw new NotFoundException("Recipe no encontrada: id=" + r.getIdRecipe());
        }
    }

    private RecipeTask buscarTaskDeRecipe(Long idRecipe, Long idRecipeTask) {
        RecipeTask task = recipeTaskRepository.findById(idRecipeTask)
                .orElseThrow(() -> new NotFoundException("RecipeTask no encontrada: id=" + idRecipeTask));

        if (task.getRecipe() == null || task.getRecipe().getIdRecipe() == null) {
            throw new NotFoundException("RecipeTask no encontrada: id=" + idRecipeTask);
        }

        if (!idRecipe.equals(task.getRecipe().getIdRecipe())) {
            throw new NotFoundException(
                    "RecipeTask no encontrada para recipe id=" + idRecipe + ", task id=" + idRecipeTask
            );
        }

        return task;
    }

    private void copiarCamposEditables(RecipeTask origen, RecipeTask destino) {
        if (origen == null) return;

        // en update global permitís cambiar recipe
        destino.setRecipe(origen.getRecipe());

        destino.setIdProcess(origen.getIdProcess());
        destino.setIdProcessDescription(origen.getIdProcessDescription());
        destino.setIdDrawing(origen.getIdDrawing());
        destino.setOrden(origen.getOrden());
        destino.setTiempoMinutos(origen.getTiempoMinutos());
        destino.setEsCorrelativa(origen.getEsCorrelativa());
        destino.setObservaciones(origen.getObservaciones());
    }

    private void copiarCamposEditablesSinRecipe(RecipeTask origen, RecipeTask destino) {
        if (origen == null) return;

        // NO tocar recipe acá
        destino.setIdProcess(origen.getIdProcess());
        destino.setIdProcessDescription(origen.getIdProcessDescription());
        destino.setIdDrawing(origen.getIdDrawing());
        destino.setOrden(origen.getOrden());
        destino.setTiempoMinutos(origen.getTiempoMinutos());
        destino.setEsCorrelativa(origen.getEsCorrelativa());
        destino.setObservaciones(origen.getObservaciones());
    }
}
