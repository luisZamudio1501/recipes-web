package com.luis.recipes_web.service.impl;

import com.luis.recipes_web.dominio.PartNumber;
import com.luis.recipes_web.dominio.Recipe;
import com.luis.recipes_web.dto.recipe.RecipeRequestDTO;
import com.luis.recipes_web.exception.NotFoundException;
import com.luis.recipes_web.repositorio.PartNumberRepository;
import com.luis.recipes_web.repositorio.RecipeRepository;
import com.luis.recipes_web.service.RecipeService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RecipeServiceImpl implements RecipeService {

    private static final int MAX_PAGE_SIZE = 50;
    private static final int MAX_SUGGEST_LIMIT = 10;

    private final RecipeRepository recipeRepository;
    private final PartNumberRepository partNumberRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             PartNumberRepository partNumberRepository) {
        this.recipeRepository = recipeRepository;
        this.partNumberRepository = partNumberRepository;
    }

    @Override
    public Recipe create(RecipeRequestDTO request) {
        Recipe recipe = new Recipe();
        copiarCamposEditables(request, recipe);

        // Regla: PartNumber obligatorio + existente + activo
        validarPartNumberObligatorioYActivo(recipe);

        return recipeRepository.save(recipe);
    }

    @Override
    public Recipe update(Long idRecipe, RecipeRequestDTO request) {
        Recipe existente = recipeRepository.findById(idRecipe)
                .orElseThrow(() -> new NotFoundException("Recipe no encontrada: id=" + idRecipe));

        copiarCamposEditables(request, existente);

        // Regla: PartNumber obligatorio + existente + activo
        validarPartNumberObligatorioYActivo(existente);

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

    // =========================
    // HITO 7: SEARCH + SUGGEST
    // =========================

    @Override
    @Transactional(readOnly = true)
    public Page<Recipe> search(Boolean activa, String q, Pageable pageable) {
        String nq = normalize(q);

        int requestedSize = pageable == null ? 20 : pageable.getPageSize();
        int safeSize = Math.min(Math.max(requestedSize, 1), MAX_PAGE_SIZE);

        int requestedPage = pageable == null ? 0 : pageable.getPageNumber();
        int safePage = Math.max(requestedPage, 0);

        Pageable fixed = PageRequest.of(
                safePage,
                safeSize,
                Sort.by("idRecipe").ascending()
        );

        return recipeRepository.search(activa, nq, fixed);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SuggestItem> suggest(Boolean activa, String q, Integer limit) {
        String nq = normalize(q);

        int lim = (limit == null) ? MAX_SUGGEST_LIMIT : Math.min(Math.max(limit, 1), MAX_SUGGEST_LIMIT);

        Pageable topN = PageRequest.of(0, lim);

        return recipeRepository.suggest(activa, nq, topN).stream()
                .map(r -> {
                    PartNumber pn = r.getPartNumber();
                    String codigo = (pn != null ? pn.getCodigoPartNumber() : "");
                    String nombre = (pn != null ? pn.getNombrePartNumber() : "");
                    String label = codigo + " - " + nombre + " (Recipe " + r.getIdRecipe() + ")";
                    return new SuggestItem(
                            r.getIdRecipe(),
                            label,
                            codigo,
                            r.getActiva()
                    );
                })
                .toList();
    }

    private String normalize(String q) {
        if (q == null) return null;
        String t = q.trim().replaceAll("\\s+", " ");
        return t.isEmpty() ? null : t;
    }

    // =======================
    // Métodos privados
    // =======================

    private void validarPartNumberObligatorioYActivo(Recipe recipe) {
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe es null");
        }

        PartNumber pn = recipe.getPartNumber();
        if (pn == null || pn.getIdPart() == null) {
            throw new IllegalArgumentException("Recipe debe tener PartNumber (id_part) válido");
        }

        PartNumber existente = partNumberRepository.findById(pn.getIdPart())
                .orElseThrow(() -> new NotFoundException("PartNumber no encontrado: id=" + pn.getIdPart()));

        if (existente.getActivo() == null || !existente.getActivo()) {
            throw new IllegalArgumentException("PartNumber inactivo: id=" + existente.getIdPart());
        }
    }

    private void copiarCamposEditables(RecipeRequestDTO origen, Recipe destino) {
        if (origen == null) return;

        PartNumber pn = null;
        if (origen.getIdPart() != null) {
            pn = new PartNumber();
            pn.setIdPart(origen.getIdPart());
        }

        destino.setPartNumber(pn);
        destino.setFechaCreacion(origen.getFechaCreacion());
        destino.setObservaciones(origen.getObservaciones());
        destino.setActiva(origen.getActiva());
    }
}
