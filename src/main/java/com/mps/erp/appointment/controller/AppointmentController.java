package com.mps.erp.appointment.controller;

import com.mps.erp.appointment.dto.AppointmentRequest;
import com.mps.erp.appointment.dto.AppointmentResponse;
import com.mps.erp.appointment.dto.DoctorScheduleRequest;
import com.mps.erp.appointment.model.AppointmentStatus;
import com.mps.erp.appointment.model.DoctorSchedule;
import com.mps.erp.appointment.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_CONTABLE', 'MEDICO', 'RECEPCIONISTA')")
    public ResponseEntity<AppointmentResponse> createAppointment(@RequestBody AppointmentRequest request) {
        return ResponseEntity.ok(appointmentService.createAppointment(request));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_CONTABLE', 'MEDICO', 'RECEPCIONISTA')")
    public ResponseEntity<AppointmentResponse> updateStatus(@PathVariable Long id,
                                                            @RequestParam AppointmentStatus status) {
        return ResponseEntity.ok(appointmentService.updateStatus(id, status));
    }

    @PostMapping("/{id}/attend")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_CONTABLE', 'MEDICO', 'RECEPCIONISTA')")
    public ResponseEntity<AppointmentResponse> attendAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.attendAppointment(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_CONTABLE', 'MEDICO', 'RECEPCIONISTA')")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByMonth(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam(required = false) Long doctorId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByMonth(year, month, doctorId));
    }

    @GetMapping("/schedules")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_CONTABLE', 'MEDICO', 'RECEPCIONISTA')")
    public ResponseEntity<List<DoctorSchedule>> getDoctorSchedules(
            @RequestParam(required = false) Long doctorId) {
        return ResponseEntity.ok(appointmentService.getDoctorSchedules(doctorId));
    }

    @PostMapping("/schedules")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_CONTABLE', 'MEDICO', 'RECEPCIONISTA')")
    public ResponseEntity<DoctorSchedule> addDoctorSchedule(@RequestBody DoctorScheduleRequest request) {
        return ResponseEntity.ok(appointmentService.addDoctorSchedule(request));
    }
}