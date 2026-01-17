package com.luis.recipes_web.controller;

import com.luis.recipes_web.dominio.PartNumber;
import com.luis.recipes_web.dto.partnumber.PartNumberRequestDTO;
import com.luis.recipes_web.dto.partnumber.PartNumberResponseDTO;
import com.luis.recipes_web.mapper.PartNumberMapper;
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
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<PartNumberResponseDTO> page =
                partNumberService.search(activo, q, pageable)
                        .map(PartNumberMapper::toResponse);

        return ResponseEntity.ok(page);
    }

    // GET - autocompletado incremental (suggest)
    @GetMapping("/suggest")
    public ResponseEntity<List<PartNumberService.SuggestItem>> suggest(
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer limit
    ) {
        return ResponseEntity.ok(
                partNumberService.suggest(activo, q, limit)
        );
    }

    // GET - obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<PartNumberResponseDTO> getById(
            @PathVariable("id") Long idPart) {

        return partNumberService.findById(idPart)
                .map(PartNumberMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET - obtener por código
    @GetMapping("/by-codigo/{codigo}")
    public ResponseEntity<PartNumberResponseDTO> getByCodigo(
            @PathVariable("codigo") String codigoPartNumber) {

        return partNumberService.findByCodigo(codigoPartNumber)
                .map(PartNumberMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST - crear
    @PostMapping
    public ResponseEntity<PartNumberResponseDTO> create(
            @Valid @RequestBody PartNumberRequestDTO request) {

        PartNumber entity = PartNumberMapper.toEntity(request);
        PartNumber created = partNumberService.create(entity);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(PartNumberMapper.toResponse(created));
    }

    // PUT - actualizar
    @PutMapping("/{id}")
    public ResponseEntity<PartNumberResponseDTO> update(
            @PathVariable("id") Long idPart,
            @Valid @RequestBody PartNumberRequestDTO request
    ) {
        PartNumber entity = PartNumberMapper.toEntity(request);
        PartNumber updated = partNumberService.update(idPart, entity);

        return ResponseEntity.ok(
                PartNumberMapper.toResponse(updated)
        );
    }

    // DELETE - eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") Long idPart) {

        partNumberService.delete(idPart);
        return ResponseEntity.noContent().build();
    }
}
