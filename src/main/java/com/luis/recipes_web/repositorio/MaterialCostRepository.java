package com.luis.recipes_web.repositorio;

import com.luis.recipes_web.dominio.MaterialCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MaterialCostRepository extends JpaRepository<MaterialCost, Long> {

    @Query("""
        SELECT mc
        FROM MaterialCost mc
        WHERE mc.material.idMaterial = :idMaterial
          AND mc.vigenciaDesde <= :fecha
          AND (mc.vigenciaHasta IS NULL OR mc.vigenciaHasta >= :fecha)
        ORDER BY mc.vigenciaDesde DESC
        """)
    Optional<MaterialCost> findCurrentByMaterial(@Param("idMaterial") Long idMaterial,
                                                 @Param("fecha") LocalDate fecha);

    List<MaterialCost> findByMaterial_IdMaterialOrderByVigenciaDesdeDesc(Long idMaterial);

    @Query("""
        SELECT COUNT(mc)
        FROM MaterialCost mc
        WHERE mc.material.idMaterial = :idMaterial
          AND mc.vigenciaDesde <= :newHasta
          AND COALESCE(mc.vigenciaHasta, :maxDate) >= :newDesde
        """)
    long countOverlapsNormalized(@Param("idMaterial") Long idMaterial,
                                 @Param("newDesde") LocalDate newDesde,
                                 @Param("newHasta") LocalDate newHasta,
                                 @Param("maxDate") LocalDate maxDate);
}
