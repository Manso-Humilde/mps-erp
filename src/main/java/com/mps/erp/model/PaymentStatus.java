package com.mps.erp.model;

public enum PaymentStatus {
    PENDIENTE("Pendiente"),
    PARCIAL("Parcial"),
    COMPLETADO("Completado"),
    ANULADO("Anulado");

    private final String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}