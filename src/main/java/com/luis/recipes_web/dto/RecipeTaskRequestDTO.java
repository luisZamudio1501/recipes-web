package com.luis.recipes_web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RecipeTaskRequestDTO {

    @NotBlank(message = "El campo 'title' es obligatorio")
    @Size(max = 120, message = "El campo 'title' no puede superar 120 caracteres")
    private String title;

    @Size(max = 1000, message = "El campo 'description' no puede superar 1000 caracteres")
    private String description;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
