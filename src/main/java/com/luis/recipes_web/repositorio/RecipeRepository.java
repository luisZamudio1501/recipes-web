package com.luis.recipes_web.repositorio;

import com.luis.recipes_web.dominio.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("select r from Recipe r join fetch r.partNumber")
    List<Recipe> findAllWithPartNumber();

    @Query("select r from Recipe r join fetch r.partNumber where r.idRecipe = :id")
    Optional<Recipe> findByIdWithPartNumber(@Param("id") Long id);
}
