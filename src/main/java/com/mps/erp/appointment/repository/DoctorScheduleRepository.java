package com.mps.erp.appointment.repository;

import com.mps.erp.appointment.model.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {
    List<DoctorSchedule> findByTenantIdAndDoctorIdAndActivoTrue(Long tenantId, Long doctorId);
    List<DoctorSchedule> findByTenantIdAndActivoTrue(Long tenantId);
}