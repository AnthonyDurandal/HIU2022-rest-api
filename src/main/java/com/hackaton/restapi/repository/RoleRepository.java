package com.hackaton.restapi.repository;

import java.util.Optional;

import com.hackaton.restapi.entity.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Long>,
        JpaSpecificationExecutor<Role> {

    @Query("SELECT r FROM Role r WHERE r.nom = ?1")
    Optional<Role> findByNom(String role);
}
