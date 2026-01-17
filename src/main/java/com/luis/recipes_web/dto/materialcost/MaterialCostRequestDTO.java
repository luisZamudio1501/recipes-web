package com.luis.recipes_web.dto.materialcost;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class MaterialCostRequestDTO {

    @NotNull
    @Positive
    private Long idMaterial;

    @NotNull
    @DecimalMin(value = "0.000000", inclusive = false)
    private BigDecimal costoUnitario;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z]{3}$", message = "La moneda debe tener 3 letras (ej: ARS, USD).")
    private String moneda;

    @NotNull
    private LocalDate vigenciaDesde;

    private LocalDate vigenciaHasta;

    @Size(max = 120)
    private String proveedor;

    @Size(max = 255)
    private String observacion;

    @AssertTrue(message = "vigenciaHasta debe ser null o mayor/igual que vigenciaDesde.")
    public boolean isRangoVigenciaValido() {
        if (vigenciaHasta == null) return true;
        if (vigenciaDesde == null) return true; // lo valida @NotNull
        return !vigenciaHasta.isBefore(vigenciaDesde);
    }

    public Long getIdMaterial() { return idMaterial; }
    public void setIdMaterial(Long idMaterial) { this.idMaterial = idMaterial; }

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
}
