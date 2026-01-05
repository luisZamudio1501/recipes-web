package com.luis.recipes_web.controller;

import com.luis.recipes_web.dominio.PartNumber;
import com.luis.recipes_web.service.PartNumberService;
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

    // GET - listar todos
    @GetMapping
    public ResponseEntity<List<PartNumber>> getAll() {
        return ResponseEntity.ok(partNumberService.findAll());
    }

    // GET - obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<PartNumber> getById(@PathVariable("id") Long idPart) {
        return partNumberService.findById(idPart)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET - obtener por código
    @GetMapping("/by-codigo/{codigo}")
    public ResponseEntity<PartNumber> getByCodigo(@PathVariable("codigo") String codigoPartNumber) {
        return partNumberService.findByCodigo(codigoPartNumber)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST - crear
    @PostMapping
    public ResponseEntity<PartNumber> create(@RequestBody PartNumber partNumber) {
        PartNumber created = partNumberService.create(partNumber);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT - actualizar
    @PutMapping("/{id}")
    public ResponseEntity<PartNumber> update(
            @PathVariable("id") Long idPart,
            @RequestBody PartNumber partNumber
    ) {
        PartNumber updated = partNumberService.update(idPart, partNumber);
        return ResponseEntity.ok(updated);
    }

    // DELETE - eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long idPart) {
        partNumberService.delete(idPart);
        return ResponseEntity.noContent().build();
    }
}
