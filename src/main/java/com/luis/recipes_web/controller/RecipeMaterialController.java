package com.luis.recipes_web.controller;

import com.luis.recipes_web.dto.recipematerial.RecipeMaterialRequestDTO;
import com.luis.recipes_web.dto.recipematerial.RecipeMaterialResponseDTO;
import com.luis.recipes_web.service.RecipeMaterialService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipe-materials")
public class RecipeMaterialController {

    private final RecipeMaterialService recipeMaterialService;

    public RecipeMaterialController(RecipeMaterialService recipeMaterialService) {
        this.recipeMaterialService = recipeMaterialService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeMaterialResponseDTO create(@Valid @RequestBody RecipeMaterialRequestDTO request) {
        return recipeMaterialService.addMaterialToRecipe(request);
    }

    @GetMapping("/by-recipe/{idRecipe}")
    public List<RecipeMaterialResponseDTO> listByRecipe(@PathVariable Long idRecipe) {
        return recipeMaterialService.listMaterialsByRecipe(idRecipe);
    }

    @DeleteMapping("/{idRecipeMaterial}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long idRecipeMaterial) {
        recipeMaterialService.deleteRecipeMaterial(idRecipeMaterial);
    }
}
