package com.luis.recipes_web.dto.partnumber;

public class PartNumberResponseDTO {

    private Long idPart;
    private String codigoPartNumber;
    private String nombrePartNumber;
    private Boolean activo;

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
}
