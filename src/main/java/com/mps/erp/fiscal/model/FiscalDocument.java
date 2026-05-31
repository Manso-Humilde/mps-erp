package com.mps.erp.fiscal.model;

import com.mps.erp.infrastructure.TenantAwareEntity;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "fiscal_documents")
public class FiscalDocument extends TenantAwareEntity {
    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(nullable = false, unique = true)
    private String ncf;

    @Column(name = "tipo_ecf", nullable = false)
    private String tipoEcf;

    @Column(name = "rnc_emisor", nullable = false)
    private String rncEmisor;

    @Column(name = "rnc_receptor")
    private String rncReceptor;

    @Column(name = "monto_bruto", nullable = false)
    private BigDecimal montoBruto;

    @Column(name = "monto_itbis")
    private BigDecimal montoItbis = BigDecimal.ZERO;

    @Column(name = "monto_exento")
    private BigDecimal montoExento = BigDecimal.ZERO;

    @Column(name = "monto_total", nullable = false)
    private BigDecimal montoTotal;

    @Column(name = "xml_content", columnDefinition = "TEXT")
    private String xmlContent;

    @Column(name = "track_id")
    private String trackId;

    @Column(name = "estado_dgii")
    private String estadoDgii = "PENDIENTE";

    @Column(name = "mensaje_dgii", columnDefinition = "TEXT")
    private String mensajeDgii;

    @Column(name = "fecha_emision")
    private LocalDateTime fechaEmision = LocalDateTime.now();

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
    public void setNcf(String ncf) { this.ncf = ncf; }
    public String getNcf() { return ncf; }
    public void setTipoEcf(String tipoEcf) { this.tipoEcf = tipoEcf; }
    public void setRncEmisor(String rncEmisor) { this.rncEmisor = rncEmisor; }
    public void setRncReceptor(String rncReceptor) { this.rncReceptor = rncReceptor; }
    public void setMontoBruto(BigDecimal montoBruto) { this.montoBruto = montoBruto; }
    public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; }
    public void setMontoItbis(BigDecimal montoItbis) { this.montoItbis = montoItbis; }
    public void setMontoExento(BigDecimal montoExento) { this.montoExento = montoExento; }
    public void setXmlContent(String xmlContent) { this.xmlContent = xmlContent; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }
    public void setTrackId(String trackId) { this.trackId = trackId; }
    public void setEstadoDgii(String estadoDgii) { this.estadoDgii = estadoDgii; }
    public void setMensajeDgii(String mensajeDgii) { this.mensajeDgii = mensajeDgii; }

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}
