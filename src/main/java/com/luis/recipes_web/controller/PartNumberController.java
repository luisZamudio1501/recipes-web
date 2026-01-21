package com.luis.recipes_web.controller;

import com.luis.recipes_web.dto.common.SuggestItemDTO;
import com.luis.recipes_web.dto.partnumber.PartNumberRequestDTO;
import com.luis.recipes_web.dto.partnumber.PartNumberResponseDTO;
import com.luis.recipes_web.service.PartNumberService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/part-numbers")
public class PartNumberController {

    private final PartNumberService partNumberService;

    public PartNumberController(PartNumberService partNumberService) {
        this.partNumberService = partNumberService;
    }

    // GET - búsqueda paginada (q + activo)
    @GetMapping
    public ResponseEntity<Page<PartNumberResponseDTO>> search(
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false) String q,
            @PageableDefault(size = 20, sort = {"codigoPartNumber", "nombrePartNumber"}) Pageable pageable
    ) {
        return ResponseEntity.ok(partNumberService.search(activo, q, pageable));
    }

    // GET - autocompletado incremental (suggest)
    @GetMapping("/suggest")
    public ResponseEntity<List<SuggestItemDTO>> suggest(
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer limit
    ) {
        return ResponseEntity.ok(partNumberService.suggest(activo, q, limit));
    }

    // GET - obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<PartNumberResponseDTO> getById(@PathVariable("id") Long idPart) {
        return ResponseEntity.ok(partNumberService.findById(idPart));
    }

    // GET - obtener por código
    @GetMapping("/by-codigo/{codigo}")
    public ResponseEntity<PartNumberResponseDTO> getByCodigo(@PathVariable("codigo") String codigoPartNumber) {
        return ResponseEntity.ok(partNumberService.findByCodigo(codigoPartNumber));
    }

    // POST - crear
    @PostMapping
    public ResponseEntity<PartNumberResponseDTO> create(@Valid @RequestBody PartNumberRequestDTO request) {
        PartNumberResponseDTO created = partNumberService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT - actualizar
    @PutMapping("/{id}")
    public ResponseEntity<PartNumberResponseDTO> update(
            @PathVariable("id") Long idPart,
            @Valid @RequestBody PartNumberRequestDTO request
    ) {
        return ResponseEntity.ok(partNumberService.update(idPart, request));
    }

    // DELETE - SOFT delete (marca activo=false)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long idPart) {
        partNumberService.delete(idPart);
        return ResponseEntity.noContent().build();
    }

    // DELETE - HARD delete (uso excepcional)
    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDelete(@PathVariable("id") Long idPart) {
        partNumberService.hardDelete(idPart);
        return ResponseEntity.noContent().build();
    }
}
