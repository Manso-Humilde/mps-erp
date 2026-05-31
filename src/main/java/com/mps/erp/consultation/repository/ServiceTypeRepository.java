package com.mps.erp.consultation.repository;

import com.mps.erp.consultation.model.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> {
}