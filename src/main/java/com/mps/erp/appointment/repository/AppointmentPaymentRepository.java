package com.mps.erp.appointment.repository;

import com.mps.erp.appointment.model.Appointment;
import com.mps.erp.appointment.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentPaymentRepository extends JpaRepository<Appointment, Long> {

    // AÑADIR ESTE MÉTODO ↓
    List<Appointment> findByTenantIdAndFechaHoraInicioBetween(Long tenantId, LocalDateTime inicio, LocalDateTime fin);

    List<Appointment> findByTenantIdAndDoctorIdAndFechaHoraInicioBetween(
            Long tenantId, Long doctorId, LocalDateTime inicio, LocalDateTime fin);

    List<Appointment> findByTenantIdAndPatientId(Long tenantId, Long patientId);

    List<Appointment> findByTenantIdAndStatus(Long tenantId, AppointmentStatus status);

    @Query("SELECT a FROM Appointment a WHERE a.tenantId = :tenantId AND a.doctorId = :doctorId " +
            "AND a.fechaHoraInicio = :fechaHoraInicio AND a.status NOT IN ('CANCELADA', 'NO_ASISTIO')")
    List<Appointment> findConflictingAppointments(@Param("tenantId") Long tenantId,
                                                  @Param("doctorId") Long doctorId,
                                                  @Param("fechaHoraInicio") LocalDateTime fechaHoraInicio);
}