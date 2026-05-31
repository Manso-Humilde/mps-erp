package com.mps.erp.consultation.controller;

import com.mps.erp.consultation.model.ServiceType;
import com.mps.erp.consultation.repository.ServiceTypeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/service-types")
public class ServiceTypeController {

    private final ServiceTypeRepository serviceTypeRepository;

    public ServiceTypeController(ServiceTypeRepository serviceTypeRepository) {
        this.serviceTypeRepository = serviceTypeRepository;
    }

    @GetMapping
    public List<ServiceType> getAllServiceTypes() {
        return serviceTypeRepository.findAll();
    }
}