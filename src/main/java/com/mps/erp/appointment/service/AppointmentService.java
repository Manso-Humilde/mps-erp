package com.mps.erp.appointment.service;

import com.mps.erp.appointment.dto.AppointmentRequest;
import com.mps.erp.appointment.dto.AppointmentResponse;
import com.mps.erp.appointment.dto.DoctorScheduleRequest;
import com.mps.erp.appointment.model.Appointment;
import com.mps.erp.appointment.model.AppointmentStatus;
import com.mps.erp.appointment.model.DoctorSchedule;
import com.mps.erp.appointment.repository.AppointmentPaymentRepository;
import com.mps.erp.appointment.repository.DoctorScheduleRepository;
import com.mps.erp.consultation.dto.ConsultationRequest;
import com.mps.erp.consultation.dto.ConsultationResponse;

import com.mps.erp.consultation.service.ConsultationService;
import com.mps.erp.infrastructure.tenant.TenantContext;

import com.mps.erp.repository.DoctorRepository;
import com.mps.erp.repository.PatientRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentPaymentRepository appointmentRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final ConsultationService consultationService;

    public AppointmentService(AppointmentPaymentRepository appointmentRepository,
                              DoctorScheduleRepository doctorScheduleRepository,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository,
                              ConsultationService consultationService) {
        this.appointmentRepository = appointmentRepository;
        this.doctorScheduleRepository = doctorScheduleRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.consultationService = consultationService;
    }

    @Transactional
    public AppointmentResponse createAppointment(AppointmentRequest request) {
        Long tenantId = TenantContext.getCurrentTenant();

       // validateWorkingHours(tenantId, request.getDoctorId(), request.getFechaHoraInicio());
       // validateNoConflict(tenantId, request.getDoctorId(), request.getFechaHoraInicio());

        // Duración fija de 30 minutos por ahora
        LocalDateTime fechaHoraFin = request.getFechaHoraInicio().plusMinutes(30);

        Appointment appointment = new Appointment();
        appointment.setTenantId(tenantId);
        appointment.setPatientId(request.getPatientId());
        appointment.setDoctorId(request.getDoctorId());
        appointment.setServiceTypeId(request.getServiceTypeId());
        appointment.setFechaHoraInicio(request.getFechaHoraInicio());
        appointment.setFechaHoraFin(fechaHoraFin);
        appointment.setStatus(AppointmentStatus.PENDIENTE);
        appointment.setMotivo(request.getMotivo());
        appointment.setNotas(request.getNotas());

        return toResponse(appointmentRepository.save(appointment));
    }

    @Transactional
    public AppointmentResponse updateStatus(Long appointmentId, AppointmentStatus newStatus) {
        Long tenantId = TenantContext.getCurrentTenant();
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (!appointment.getTenantId().equals(tenantId)) {
            throw new RuntimeException("No autorizado");
        }

        appointment.setStatus(newStatus);
        return toResponse(appointmentRepository.save(appointment));
    }

    @Transactional
    public AppointmentResponse attendAppointment(Long appointmentId) {
        Long tenantId = TenantContext.getCurrentTenant();
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (!appointment.getTenantId().equals(tenantId)) {
            throw new RuntimeException("No autorizado");
        }

        if (appointment.getStatus() != AppointmentStatus.CONFIRMADA &&
                appointment.getStatus() != AppointmentStatus.PENDIENTE) {
            throw new RuntimeException("Solo se pueden atender citas en estado PENDIENTE o CONFIRMADA");
        }

        ConsultationRequest consultaRequest = new ConsultationRequest();
        consultaRequest.setPatientId(appointment.getPatientId());
        consultaRequest.setDiagnostico(appointment.getMotivo());

        ConsultationResponse consultationResponse = consultationService.createConsultation(consultaRequest);

        appointment.setStatus(AppointmentStatus.ATENDIDA);
        appointment.setConsultationId(consultationResponse.getId());

        return toResponse(appointmentRepository.save(appointment));
    }

    public List<AppointmentResponse> getAppointmentsByMonth(int year, int month, Long doctorId) {
        Long tenantId = TenantContext.getCurrentTenant();
        LocalDateTime inicio = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime fin = inicio.plusMonths(1).minusSeconds(1);

        List<Appointment> appointments;
        if (doctorId != null) {
            appointments = appointmentRepository.findByTenantIdAndDoctorIdAndFechaHoraInicioBetween(
                    tenantId, doctorId, inicio, fin);
        } else {
            appointments = appointmentRepository.findByTenantIdAndFechaHoraInicioBetween(tenantId, inicio, fin);
        }

        return appointments.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<DoctorSchedule> getDoctorSchedules(Long doctorId) {
        Long tenantId = TenantContext.getCurrentTenant();
        if (doctorId != null) {
            return doctorScheduleRepository.findByTenantIdAndDoctorIdAndActivoTrue(tenantId, doctorId);
        }
        return doctorScheduleRepository.findByTenantIdAndActivoTrue(tenantId);
    }

    @Transactional
    public DoctorSchedule addDoctorSchedule(DoctorScheduleRequest request) {
        Long tenantId = TenantContext.getCurrentTenant();
        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setTenantId(tenantId);
        schedule.setDoctorId(request.getDoctorId());
        schedule.setDiaSemana(request.getDiaSemana());
        schedule.setHoraInicio(request.getHoraInicio());
        schedule.setHoraFin(request.getHoraFin());
        schedule.setIntervaloMinutos(request.getIntervaloMinutos() != null ? request.getIntervaloMinutos() : 30);
        schedule.setActivo(request.getActivo() != null ? request.getActivo() : true);
        return doctorScheduleRepository.save(schedule);
    }

    private void validateWorkingHours(Long tenantId, Long doctorId, LocalDateTime fechaHora) {
        int diaSemana = fechaHora.getDayOfWeek().getValue();
        LocalTime hora = fechaHora.toLocalTime();

        List<DoctorSchedule> schedules = doctorScheduleRepository.findByTenantIdAndDoctorIdAndActivoTrue(tenantId, doctorId);

        boolean dentroHorario = schedules.stream().anyMatch(s ->
                s.getDiaSemana().equals(diaSemana) &&
                        !hora.isBefore(s.getHoraInicio()) &&
                        !hora.isAfter(s.getHoraFin())
        );

        if (!dentroHorario) {
            throw new RuntimeException("El médico no está disponible en este horario");
        }
    }

    private void validateNoConflict(Long tenantId, Long doctorId, LocalDateTime fechaHora) {
        List<Appointment> conflicts = appointmentRepository.findConflictingAppointments(tenantId, doctorId, fechaHora);
        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Ya existe una cita programada para este médico en este horario");
        }
    }

    @PutMapping("/{id}/consultation")
    public ResponseEntity<?> updateConsultationId(@PathVariable Long id, @RequestBody Map<String, Long> body) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow();
        appointment.setConsultationId(body.get("consultationId"));
        appointmentRepository.save(appointment);
        return ResponseEntity.ok().build();
    }

    private AppointmentResponse toResponse(Appointment appointment) {
        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setTenantId(appointment.getTenantId());
        response.setPatientId(appointment.getPatientId());
        response.setDoctorId(appointment.getDoctorId());
        response.setServiceTypeId(appointment.getServiceTypeId());
        response.setFechaHoraInicio(appointment.getFechaHoraInicio());
        response.setFechaHoraFin(appointment.getFechaHoraFin());
        response.setStatus(appointment.getStatus());
        response.setMotivo(appointment.getMotivo());
        response.setNotas(appointment.getNotas());
        response.setConsultationId(appointment.getConsultationId());

        patientRepository.findById(appointment.getPatientId()).ifPresent(p ->
                response.setPatientName(p.getNombreCompleto()));
        doctorRepository.findById(appointment.getDoctorId()).ifPresent(d ->
                response.setDoctorName(d.getNombreCompleto()));

        return response;
    }
}