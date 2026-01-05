package com.luis.recipes_web.dominio;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "part_number",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_part_number_codigo",
                        columnNames = "codigo_part_number"
                )
        },
        indexes = {
                @Index(name = "idx_part_number_codigo", columnList = "codigo_part_number"),
                @Index(name = "idx_part_number_nombre", columnList = "nombre_part_number")
        }
)
public class PartNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_part", nullable = false)
    private Long idPart;

    @Column(name = "codigo_part_number", nullable = false, length = 50)
    private String codigoPartNumber;

    @Column(name = "nombre_part_number", nullable = false, length = 255)
    private String nombrePartNumber;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public PartNumber() {
    }

    // Getters y setters

    public Long getIdPart() {
        return idPart;
    }

    public void setIdPart(Long idPart) {
        this.idPart = idPart;
    }

    public String getCodigoPartNumber() {
        return codigoPartNumber;
    }

    public void setCodigoPartNumber(String codigoPartNumber) {
        this.codigoPartNumber = codigoPartNumber;
    }

    public String getNombrePartNumber() {
        return nombrePartNumber;
    }

    public void setNombrePartNumber(String nombrePartNumber) {
        this.nombrePartNumber = nombrePartNumber;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }





    @PrePersist
    protected void onCreate() {
        if (this.activo == null) this.activo = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PartNumber)) return false;
        PartNumber that = (PartNumber) o;
        return idPart != null && idPart.equals(that.idPart);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


}
