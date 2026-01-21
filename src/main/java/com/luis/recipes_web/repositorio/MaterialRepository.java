package com.luis.recipes_web.repositorio;

import com.luis.recipes_web.dominio.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MaterialRepository extends JpaRepository<Material, Long> {

    Optional<Material> findByCodigoMaterial(String codigoMaterial);

    boolean existsByCodigoMaterial(String codigoMaterial);

    // === SEARCH (filtrado + paginado) ===
    @Query("""
        SELECT m
        FROM Material m
        WHERE (:activo IS NULL OR m.activo = :activo)
          AND (
                :q IS NULL
                OR LOWER(m.codigoMaterial) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(m.nombre) LIKE LOWER(CONCAT('%', :q, '%'))
          )
    """)
    Page<Material> search(
            @Param("activo") Boolean activo,
            @Param("q") String q,
            Pageable pageable
    );

    // === SUGGEST (autocompletado incremental) ===
    @Query("""
        SELECT m
        FROM Material m
        WHERE (:activo IS NULL OR m.activo = :activo)
          AND (
                LOWER(m.codigoMaterial) LIKE LOWER(CONCAT(:q, '%'))
                OR LOWER(m.nombre) LIKE LOWER(CONCAT(:q, '%'))
          )
        ORDER BY m.codigoMaterial ASC, m.nombre ASC
    """)
    List<Material> suggest(
            @Param("activo") Boolean activo,
            @Param("q") String q,
            Pageable pageable
    );
}
