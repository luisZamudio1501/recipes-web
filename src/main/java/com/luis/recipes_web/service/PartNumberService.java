package com.luis.recipes_web.service;

import com.luis.recipes_web.dominio.PartNumber;

import java.util.List;
import java.util.Optional;

public interface PartNumberService {

    PartNumber create(PartNumber partNumber);

    PartNumber update(Long idPart, PartNumber partNumber);

    void delete(Long idPart);

    Optional<PartNumber> findById(Long idPart);

    Optional<PartNumber> findByCodigo(String codigoPartNumber);

    List<PartNumber> findAll();
}
