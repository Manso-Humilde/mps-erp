package com.mps.erp.repository;

import com.mps.erp.model.SystemConfig;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemConfigRepository extends CrudRepository<SystemConfig, Long> {

    Optional<SystemConfig> findByClave(String clave);

    @Query("SELECT valor FROM system_config WHERE clave = :clave")
    Optional<String> findValorByClave(@Param("clave") String clave);
}