package com.mps.erp.model;

public enum DGIIStatus {
    PENDIENTE("Pendiente"),
    PENDIENTE_ENVIO("Pendiente de Envío"),
    EN_PROCESO("En Proceso"),
    PROCESANDO("Procesando"),
    APROBADA("Aprobada"),
    RECHAZADA("Rechazada");

    private final String displayName;

    DGIIStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}