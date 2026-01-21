package com.luis.recipes_web.service.impl;

import com.luis.recipes_web.dominio.PartNumber;
import com.luis.recipes_web.dominio.Recipe;
import com.luis.recipes_web.dto.common.SuggestItemDTO;
import com.luis.recipes_web.dto.recipe.RecipeRequestDTO;
import com.luis.recipes_web.dto.recipe.RecipeResponseDTO;
import com.luis.recipes_web.exception.NotFoundException;
import com.luis.recipes_web.mapper.RecipeMapper;
import com.luis.recipes_web.repositorio.PartNumberRepository;
import com.luis.recipes_web.repositorio.RecipeRepository;
import com.luis.recipes_web.service.RecipeService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public RecipeResponseDTO create(RecipeRequestDTO request) {
        Recipe recipe = new Recipe();
        copiarCamposEditables(request, recipe);

        validarPartNumberObligatorioYActivoYSetear(recipe);

        Recipe saved = recipeRepository.save(recipe);
        return RecipeMapper.toResponse(saved);
    }

    @Override
    public RecipeResponseDTO update(Long idRecipe, RecipeRequestDTO request) {
        Recipe existente = recipeRepository.findById(idRecipe)
                .orElseThrow(() -> new NotFoundException("Recipe no encontrada: id=" + idRecipe));

        copiarCamposEditables(request, existente);

        validarPartNumberObligatorioYActivoYSetear(existente);

        Recipe saved = recipeRepository.save(existente);
        return RecipeMapper.toResponse(saved);
    }

    // HARD DELETE (físico)
    @Override
    public void delete(Long idRecipe) {
        Recipe existente = recipeRepository.findById(idRecipe)
                .orElseThrow(() -> new NotFoundException("Recipe no encontrada: id=" + idRecipe));
        recipeRepository.delete(existente);
    }

    // BORRADO LOGICO
    @Override
    public void deactivate(Long idRecipe) {
        Recipe existente = recipeRepository.findById(idRecipe)
                .orElseThrow(() -> new NotFoundException("Recipe no encontrada: id=" + idRecipe));

        if (Boolean.FALSE.equals(existente.getActiva())) {
            return;
        }

        existente.setActiva(false);
        recipeRepository.save(existente);
    }

    @Override
    @Transactional(readOnly = true)
    public RecipeResponseDTO findById(Long idRecipe) {
        Recipe r = recipeRepository.findByIdWithPartNumber(idRecipe)
                .orElseThrow(() -> new NotFoundException("Recipe no encontrada: id=" + idRecipe));
        return RecipeMapper.toResponse(r);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecipeResponseDTO> findAll() {
        return recipeRepository.findAllWithPartNumber()
                .stream()
                .map(RecipeMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RecipeResponseDTO> search(Boolean activa, String q, Pageable pageable) {
        String nq = normalize(q);

        int requestedSize = (pageable == null) ? 20 : pageable.getPageSize();
        int safeSize = Math.min(Math.max(requestedSize, 1), MAX_PAGE_SIZE);

        int requestedPage = (pageable == null) ? 0 : pageable.getPageNumber();
        int safePage = Math.max(requestedPage, 0);

        // sin sort acá (mejor ordenar en el @Query con ORDER BY)
        Pageable fixed = PageRequest.of(safePage, safeSize);

        return recipeRepository.search(activa, nq, fixed)
                .map(RecipeMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SuggestItemDTO> suggest(Boolean activa, String q, Integer limit) {
        String nq = normalize(q);
        if (nq == null) return List.of();

        int lim = (limit == null)
                ? MAX_SUGGEST_LIMIT
                : Math.min(Math.max(limit, 1), MAX_SUGGEST_LIMIT);

        Pageable topN = PageRequest.of(0, lim);

        return recipeRepository.suggest(activa, nq, topN).stream()
                .map(r -> {
                    PartNumber pn = r.getPartNumber();
                    String codigo = (pn != null ? pn.getCodigoPartNumber() : "");
                    String nombre = (pn != null ? pn.getNombrePartNumber() : "");
                    String label = codigo + " - " + nombre + " (Recipe " + r.getIdRecipe() + ")";
                    String extra = codigo;
                    return new SuggestItemDTO(r.getIdRecipe(), label, extra);
                })
                .toList();
    }

    private String normalize(String q) {
        if (q == null) return null;
        String t = q.trim().replaceAll("\\s+", " ");
        return t.isEmpty() ? null : t;
    }

    private void validarPartNumberObligatorioYActivoYSetear(Recipe recipe) {
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe es null");
        }

        PartNumber pn = recipe.getPartNumber();
        if (pn == null || pn.getIdPart() == null) {
            throw new IllegalArgumentException("Recipe debe tener PartNumber (id_part) válido");
        }

        PartNumber existente = partNumberRepository.findById(pn.getIdPart())
                .orElseThrow(() -> new NotFoundException("PartNumber no encontrado: id=" + pn.getIdPart()));

        if (!Boolean.TRUE.equals(existente.getActivo())) {
            throw new IllegalArgumentException("PartNumber inactivo: id=" + existente.getIdPart());
        }

        // clave: setear el PN real
        recipe.setPartNumber(existente);
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
