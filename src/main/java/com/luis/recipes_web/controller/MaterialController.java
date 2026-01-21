package com.luis.recipes_web.controller;

import com.luis.recipes_web.dto.common.SuggestItemDTO;
import com.luis.recipes_web.dto.material.MaterialRequestDTO;
import com.luis.recipes_web.dto.material.MaterialResponseDTO;
import com.luis.recipes_web.service.MaterialService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    /**
     * Búsqueda paginada principal.
     * Params:
     * - activo: null => no filtra
     * - q: null/vacío => sin filtro de texto
     */
    @GetMapping
    public ResponseEntity<Page<MaterialResponseDTO>> search(
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false) String q,
            @PageableDefault(size = 20, sort = {"codigoMaterial", "nombre"}) Pageable pageable

    ) {
        return ResponseEntity.ok(materialService.search(activo, q, pageable));
    }


    @GetMapping("/suggest")
    public ResponseEntity<List<SuggestItemDTO>> suggest(
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer limit
    ) {
        return ResponseEntity.ok(materialService.suggest(activo, q, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(materialService.findById(id));
    }

    @PostMapping
    public ResponseEntity<MaterialResponseDTO> create(@Valid @RequestBody MaterialRequestDTO request) {
        MaterialResponseDTO created = materialService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaterialResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody MaterialRequestDTO request
    ) {
        return ResponseEntity.ok(materialService.update(id, request));
    }

    // SOFT delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        materialService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // HARD delete (excepcional)
    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDelete(@PathVariable Long id) {
        materialService.hardDelete(id);
        return ResponseEntity.noContent().build();
    }
}
