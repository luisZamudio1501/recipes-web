package com.luis.recipes_web.service.impl;

import com.luis.recipes_web.dominio.PartNumber;
import com.luis.recipes_web.dominio.Recipe;
import com.luis.recipes_web.exception.NotFoundException;
import com.luis.recipes_web.repositorio.PartNumberRepository;
import com.luis.recipes_web.repositorio.RecipeRepository;
import com.luis.recipes_web.service.RecipeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final PartNumberRepository partNumberRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             PartNumberRepository partNumberRepository) {
        this.recipeRepository = recipeRepository;
        this.partNumberRepository = partNumberRepository;
    }

    @Override
    public Recipe create(Recipe recipe) {
        validarPartNumberObligatorio(recipe);
        return recipeRepository.save(recipe);
    }

    @Override
    public Recipe update(Long idRecipe, Recipe recipe) {
        Recipe existente = recipeRepository.findById(idRecipe)
                .orElseThrow(() -> new NotFoundException("Recipe no encontrada: id=" + idRecipe));

        copiarCamposEditables(recipe, existente);
        validarPartNumberObligatorio(existente);

        return recipeRepository.save(existente);
    }

    @Override
    public void delete(Long idRecipe) {
        Recipe existente = recipeRepository.findById(idRecipe)
                .orElseThrow(() -> new NotFoundException("Recipe no encontrada: id=" + idRecipe));
        recipeRepository.delete(existente);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Recipe> findAll() {
        return recipeRepository.findAllWithPartNumber();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Recipe> findById(Long idRecipe) {
        return recipeRepository.findByIdWithPartNumber(idRecipe);
    }


    // =======================
    // Métodos privados
    // =======================

    private void validarPartNumberObligatorio(Recipe recipe) {
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe es null");
        }

        PartNumber pn = recipe.getPartNumber();
        if (pn == null || pn.getIdPart() == null) {
            throw new IllegalArgumentException("Recipe debe tener PartNumber (id_part) válido");
        }

        boolean existe = partNumberRepository.existsById(pn.getIdPart());
        if (!existe) {
            throw new NotFoundException("PartNumber no encontrado: id=" + pn.getIdPart());
        }
    }

    private void copiarCamposEditables(Recipe origen, Recipe destino) {
        if (origen == null) return;

        // Campos editables según tu entidad
        destino.setPartNumber(origen.getPartNumber());
        destino.setFechaCreacion(origen.getFechaCreacion());
        destino.setObservaciones(origen.getObservaciones());
        destino.setActiva(origen.getActiva());

        // OJO: tasks se maneja con cuidado (por orphanRemoval/cascade)
        // En Hito 2 no lo tocamos desde acá para evitar borrados accidentales.
        // destino.setTasks(origen.getTasks());
    }
}
