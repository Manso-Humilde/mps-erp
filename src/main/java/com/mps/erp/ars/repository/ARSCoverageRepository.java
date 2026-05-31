package com.mps.erp.ars.repository;

import com.mps.erp.ars.model.ARSCoverage;
import com.mps.erp.consultation.model.ServiceType;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ARSCoverageRepository extends CrudRepository<ARSCoverage, Long> {

    @Query("SELECT * FROM ars_coverage WHERE ars_id = :arsId AND tipo_servicio = :tipoServicio AND activo = true")
    Optional<ARSCoverage> findByArsIdAndTipoServicio(
            @Param("arsId") Long arsId,
            @Param("tipoServicio") ServiceType tipoServicio
    );

    @Query("SELECT * FROM ars_coverage WHERE ars_id = :arsId AND activo = true")
    List<ARSCoverage> findByArsId(@Param("arsId") Long arsId);
}