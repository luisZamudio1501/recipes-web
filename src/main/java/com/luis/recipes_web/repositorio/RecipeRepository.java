package com.luis.recipes_web.repositorio;

import com.luis.recipes_web.dominio.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("""
           SELECT DISTINCT r
           FROM Recipe r
           JOIN FETCH r.partNumber
           """)
    List<Recipe> findAllWithPartNumber();

    @Query("""
           SELECT r
           FROM Recipe r
           JOIN FETCH r.partNumber
           WHERE r.idRecipe = :id
           """)
    Optional<Recipe> findByIdWithPartNumber(@Param("id") Long id);

    // =========================
    // HITO 7: SEARCH (paginado)
    // =========================
    @Query("""
        SELECT r
        FROM Recipe r
        JOIN r.partNumber pn
        WHERE (:activa IS NULL OR r.activa = :activa)
          AND (
                :q IS NULL
                OR LOWER(pn.codigoPartNumber) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(pn.nombrePartNumber) LIKE LOWER(CONCAT('%', :q, '%'))
          )
    """)
    Page<Recipe> search(
            @Param("activa") Boolean activa,
            @Param("q") String q,
            Pageable pageable
    );

    // =========================
    // HITO 8: SUGGEST incremental
    // (con fetch del PN para evitar N+1)
    // =========================
    @Query("""
        SELECT DISTINCT r
        FROM Recipe r
        JOIN FETCH r.partNumber pn
        WHERE (:activa IS NULL OR r.activa = :activa)
          AND (
                LOWER(pn.codigoPartNumber) LIKE LOWER(CONCAT(:q, '%'))
                OR LOWER(pn.nombrePartNumber) LIKE LOWER(CONCAT(:q, '%'))
          )
    """)
    List<Recipe> suggest(
            @Param("activa") Boolean activa,
            @Param("q") String q,
            Pageable pageable
    );
}
