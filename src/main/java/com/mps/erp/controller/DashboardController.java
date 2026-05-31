package com.mps.erp.controller;

import com.mps.erp.dto.DashboardResponse;
import com.mps.erp.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard(@RequestParam(required = false) String mes) {
        return ResponseEntity.ok(dashboardService.getDashboardData(mes));
    }
}