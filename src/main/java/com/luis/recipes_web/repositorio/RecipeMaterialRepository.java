package com.luis.recipes_web.repositorio;

import com.luis.recipes_web.dominio.RecipeMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeMaterialRepository extends JpaRepository<RecipeMaterial, Long> {

    boolean existsByRecipe_IdRecipeAndMaterial_IdMaterial(Long idRecipe, Long idMaterial);

    @Query("""
           SELECT rm
           FROM RecipeMaterial rm
           JOIN FETCH rm.material
           WHERE rm.recipe.idRecipe = :idRecipe
           ORDER BY rm.idRecipeMaterial
           """)
    List<RecipeMaterial> findByRecipeIdFetchMaterial(@Param("idRecipe") Long idRecipe);
}
