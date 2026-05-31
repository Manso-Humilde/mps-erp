package com.mps.erp.repository;

import com.mps.erp.model.SystemLog;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SystemLogRepository extends CrudRepository<SystemLog, Long> {

    @Query("SELECT * FROM system_logs WHERE user_id = :userId ORDER BY created_at DESC LIMIT 100")
    List<SystemLog> findByUserIdLimit100(@Param("userId") Long userId);

    @Query("SELECT * FROM system_logs WHERE created_at BETWEEN :startDate AND :endDate ORDER BY created_at DESC")
    List<SystemLog> findByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT * FROM system_logs ORDER BY created_at DESC LIMIT 500")
    List<SystemLog> findLast500();
}