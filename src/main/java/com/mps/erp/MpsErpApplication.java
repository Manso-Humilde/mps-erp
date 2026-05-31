package com.mps.erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MpsErpApplication {

    public static void main(String[] args) {
        SpringApplication.run(MpsErpApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("   MPS ERP - Sistema de Facturación");
        System.out.println("   República Dominicana");
        System.out.println("   Servidor iniciado en puerto 8080");
        System.out.println("========================================\n");
    }
}