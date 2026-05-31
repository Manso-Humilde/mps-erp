package com.mps.erp.model;

public enum PayerType {
    PACIENTE("Paciente"),
    ARS("ARS"),
    MIXTO("Mixto");


    private final String displayName;

    PayerType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}