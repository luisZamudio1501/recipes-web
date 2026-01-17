package com.luis.recipes_web.controller;

import com.luis.recipes_web.dto.material.MaterialRequestDTO;
import com.luis.recipes_web.dto.material.MaterialResponseDTO;
import com.luis.recipes_web.service.MaterialService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    // GET - búsqueda paginada (q + activo)
    // Ej: /api/materials?activo=true&q=acer&page=0&size=20
    @GetMapping
    public Page<MaterialResponseDTO> search(
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false) String q,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return materialService.search(activo, q, pageable);
    }

    // GET - autocompletado incremental (suggest)
    // Ej: /api/materials/suggest?q=AC&limit=10&activo=true
    @GetMapping("/suggest")
    public List<MaterialService.SuggestItem> suggest(
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer limit
    ) {
        return materialService.suggest(activo, q, limit);
    }

    @GetMapping("/{id}")
    public MaterialResponseDTO getById(@PathVariable Long id) {
        return materialService.findById(id);
    }

    @PostMapping
    public MaterialResponseDTO create(@Valid @RequestBody MaterialRequestDTO request) {
        return materialService.create(request);
    }

    @PutMapping("/{id}")
    public MaterialResponseDTO update(@PathVariable Long id, @Valid @RequestBody MaterialRequestDTO request) {
        return materialService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        materialService.delete(id);
    }
}
