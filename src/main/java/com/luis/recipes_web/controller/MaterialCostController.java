package com.luis.recipes_web.controller;

import com.luis.recipes_web.dto.materialcost.MaterialCostRequestDTO;
import com.luis.recipes_web.dto.materialcost.MaterialCostResponseDTO;
import com.luis.recipes_web.service.MaterialCostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/material-costs")
public class MaterialCostController {

    private final MaterialCostService materialCostService;

    public MaterialCostController(MaterialCostService materialCostService) {
        this.materialCostService = materialCostService;
    }

    // POST /api/material-costs
    @PostMapping
    public ResponseEntity<MaterialCostResponseDTO> create(@Valid @RequestBody MaterialCostRequestDTO request) {
        MaterialCostResponseDTO created = materialCostService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // GET /api/material-costs/material/{idMaterial}/current
    @GetMapping("/material/{idMaterial}/current")
    public ResponseEntity<MaterialCostResponseDTO> getCurrent(@PathVariable Long idMaterial) {
        return ResponseEntity.ok(materialCostService.getCurrentByMaterial(idMaterial));
    }

    // GET /api/material-costs/material/{idMaterial}
    @GetMapping("/material/{idMaterial}")
    public ResponseEntity<List<MaterialCostResponseDTO>> listByMaterial(@PathVariable Long idMaterial) {
        return ResponseEntity.ok(materialCostService.listByMaterial(idMaterial));
    }

    // DELETE /api/material-costs/{idMaterialCost}
    @DeleteMapping("/{idMaterialCost}")
    public ResponseEntity<Void> delete(@PathVariable Long idMaterialCost) {
        materialCostService.delete(idMaterialCost);
        return ResponseEntity.noContent().build();
    }

    // PUT /api/material-costs/{idMaterialCost}
    @PutMapping("/{idMaterialCost}")
    public ResponseEntity<MaterialCostResponseDTO> update(
            @PathVariable Long idMaterialCost,
            @Valid @RequestBody MaterialCostRequestDTO request
    ) {
        return ResponseEntity.ok(materialCostService.update(idMaterialCost, request));
    }

}
