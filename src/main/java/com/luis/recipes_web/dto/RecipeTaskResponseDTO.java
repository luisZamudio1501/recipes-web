package com.luis.recipes_web.dto;

public class RecipeTaskResponseDTO {
    private Long id;
    private Long recipeId;
    private String title;
    private String description;

    public RecipeTaskResponseDTO() {}

    public RecipeTaskResponseDTO(Long id, Long recipeId, String title, String description) {
        this.id = id;
        this.recipeId = recipeId;
        this.title = title;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRecipeId() { return recipeId; }
    public void setRecipeId(Long recipeId) { this.recipeId = recipeId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
