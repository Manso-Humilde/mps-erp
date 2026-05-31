package com.mps.erp.model;

public enum UserRole {
    SUPER_ADMIN("Super Administrador"),
    ADMIN_CONTABLE("Administrador Contable"),
    MEDICO("Médico"),
    ASISTENTE("Asistente");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}