package com.mps.erp.repository;

import com.mps.erp.model.User;
import com.mps.erp.model.UserRole;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByTenantIdAndActivoTrue(Long tenantId);

    List<User> findByRoleAndActivoTrue(UserRole role);

    List<User> findByActivoTrue();
//public interface UserRepository extends CrudRepository<User, Long> {
//
//    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
//    Optional<User> findByEmail(@Param("email") String email);
//
//    @Query("SELECT * FROM users WHERE doctor_id = :doctorId AND activo = true")
//    List<User> findByDoctorIdAndActivo(@Param("doctorId") Long doctorId);
//
//    @Query("SELECT * FROM users WHERE role = :role AND activo = true")
//    List<User> findByRoleAndActivo(@Param("role") UserRole role);
//
//    @Query("SELECT * FROM users WHERE activo = true")
//    List<User> findAllActivo();
}