package com.luis.recipes_web.service.impl;

import com.luis.recipes_web.dominio.PartNumber;
import com.luis.recipes_web.repositorio.PartNumberRepository;
import com.luis.recipes_web.service.PartNumberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PartNumberServiceImpl implements PartNumberService {

    private final PartNumberRepository partNumberRepository;

    public PartNumberServiceImpl(PartNumberRepository partNumberRepository) {
        this.partNumberRepository = partNumberRepository;
    }

    @Override
    public PartNumber create(PartNumber partNumber) {
        // createdAt/updatedAt/activo se manejan con @PrePersist en la entidad
        return partNumberRepository.save(partNumber);
    }

    @Override
    public PartNumber update(Long idPart, PartNumber partNumber) {
        PartNumber existing = partNumberRepository.findById(idPart)
                .orElseThrow(() -> new RuntimeException("PartNumber no encontrado: " + idPart));

        // Campos existentes en tu entidad
        existing.setCodigoPartNumber(partNumber.getCodigoPartNumber());
        existing.setNombrePartNumber(partNumber.getNombrePartNumber());
        existing.setActivo(partNumber.getActivo());

        // updatedAt se actualiza con @PreUpdate en la entidad
        return partNumberRepository.save(existing);
    }

    @Override
    public void delete(Long idPart) {
        PartNumber existing = partNumberRepository.findById(idPart)
                .orElseThrow(() -> new RuntimeException("PartNumber no encontrado: " + idPart));
        partNumberRepository.delete(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PartNumber> findById(Long idPart) {
        return partNumberRepository.findById(idPart);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PartNumber> findByCodigo(String codigoPartNumber) {
        return partNumberRepository.findByCodigoPartNumber(codigoPartNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PartNumber> findAll() {
        return partNumberRepository.findAll();
    }
}
