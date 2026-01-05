package com.luis.recipes_web.repositorio;

import com.luis.recipes_web.dominio.PartNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartNumberRepository extends JpaRepository<PartNumber, Long> {

    Optional<PartNumber> findByCodigoPartNumber(String codigoPartNumber);


}
