package com.mps.erp.repository;

import com.mps.erp.model.Payment;
import com.mps.erp.model.PaymentStatus;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LegacyPaymentRepository extends CrudRepository<Payment, Long> {

    @Query("SELECT * FROM payments WHERE doctor_id = :doctorId ORDER BY fecha_pago DESC")
    List<Payment> findByDoctorIdOrderByFechaPagoDesc(@Param("doctorId") Long doctorId);

    @Query("SELECT * FROM payments WHERE doctor_id = :doctorId AND ars_id = :arsId ORDER BY fecha_pago DESC")
    List<Payment> findByDoctorIdAndArsId(
            @Param("doctorId") Long doctorId,
            @Param("arsId") Long arsId
    );

    @Query("SELECT * FROM payments WHERE doctor_id = :doctorId AND estado = :estado")
    List<Payment> findByDoctorIdAndEstado(
            @Param("doctorId") Long doctorId,
            @Param("estado") PaymentStatus estado
    );
}