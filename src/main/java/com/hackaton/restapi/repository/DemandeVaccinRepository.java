package com.hackaton.restapi.repository;

import com.hackaton.restapi.entity.DemandeVaccin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DemandeVaccinRepository extends JpaRepository<DemandeVaccin, Long>, JpaSpecificationExecutor<DemandeVaccin> {
    
}
