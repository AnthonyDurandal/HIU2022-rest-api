package com.hackaton.restapi.repository;

import com.hackaton.restapi.entity.Historique;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HistoriqueRepository extends JpaRepository<Historique, Long>, JpaSpecificationExecutor<Historique> {
    
}
