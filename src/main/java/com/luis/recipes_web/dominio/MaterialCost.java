package com.luis.recipes_web.dominio;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "material_cost",
        indexes = {
                @Index(name = "idx_mcost_material", columnList = "id_material"),
                @Index(name = "idx_mcost_vigencia", columnList = "vigencia_desde,vigencia_hasta"),
                @Index(name = "idx_mcost_material_vig", columnList = "id_material,vigencia_desde,vigencia_hasta")
        }
)
public class MaterialCost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_material_cost", nullable = false)
    private Long idMaterialCost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_material",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_mcost_material")
    )
    private Material material;

    @Column(name = "costo_unitario", nullable = false, precision = 18, scale = 6)
    private BigDecimal costoUnitario;

    @Column(name = "moneda", nullable = false, length = 3, columnDefinition = "char(3) default 'ARS'")
    private String moneda;

    @Column(name = "vigencia_desde", nullable = false)
    private LocalDate vigenciaDesde;

    @Column(name = "vigencia_hasta")
    private LocalDate vigenciaHasta;

    @Column(name = "proveedor", length = 120)
    private String proveedor;

    @Column(name = "observacion", length = 255)
    private String observacion;

    @Generated(GenerationTime.INSERT)
    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Generated(GenerationTime.ALWAYS)
    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public Long getIdMaterialCost() { return idMaterialCost; }
    public void setIdMaterialCost(Long idMaterialCost) { this.idMaterialCost = idMaterialCost; }

    public Material getMaterial() { return material; }
    public void setMaterial(Material material) { this.material = material; }

    public BigDecimal getCostoUnitario() { return costoUnitario; }
    public void setCostoUnitario(BigDecimal costoUnitario) { this.costoUnitario = costoUnitario; }

    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }

    public LocalDate getVigenciaDesde() { return vigenciaDesde; }
    public void setVigenciaDesde(LocalDate vigenciaDesde) { this.vigenciaDesde = vigenciaDesde; }

    public LocalDate getVigenciaHasta() { return vigenciaHasta; }
    public void setVigenciaHasta(LocalDate vigenciaHasta) { this.vigenciaHasta = vigenciaHasta; }

    public String getProveedor() { return proveedor; }
    public void setProveedor(String proveedor) { this.proveedor = proveedor; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
