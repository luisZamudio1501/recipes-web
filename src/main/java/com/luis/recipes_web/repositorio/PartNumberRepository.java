package com.luis.recipes_web.repositorio;

import com.luis.recipes_web.dominio.PartNumber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PartNumberRepository extends JpaRepository<PartNumber, Long> {

    // === EXISTENTE ===
    Optional<PartNumber> findByCodigoPartNumber(String codigoPartNumber);

    // === HITO 7: SEARCH (lista filtrada, paginada) ===
    @Query("""
        SELECT pn
        FROM PartNumber pn
        WHERE (:activo IS NULL OR pn.activo = :activo)
          AND (
                :q IS NULL
                OR LOWER(pn.codigoPartNumber) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(pn.nombrePartNumber) LIKE LOWER(CONCAT('%', :q, '%'))
          )
        ORDER BY pn.codigoPartNumber ASC, pn.nombrePartNumber ASC
    """)
    Page<PartNumber> search(
            @Param("activo") Boolean activo,
            @Param("q") String q,
            Pageable pageable
    );

    // === HITO 7: SUGGEST (autocompletado incremental) ===
    @Query("""
        SELECT pn
        FROM PartNumber pn
        WHERE (:activo IS NULL OR pn.activo = :activo)
          AND (
                :q IS NULL
                OR LOWER(pn.codigoPartNumber) LIKE LOWER(CONCAT(:q, '%'))
                OR LOWER(pn.nombrePartNumber) LIKE LOWER(CONCAT(:q, '%'))
          )
        ORDER BY pn.codigoPartNumber ASC, pn.nombrePartNumber ASC
    """)
    List<PartNumber> suggest(
            @Param("activo") Boolean activo,
            @Param("q") String q,
            Pageable pageable
    );
}
