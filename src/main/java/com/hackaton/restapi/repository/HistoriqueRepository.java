package com.hackaton.restapi.repository;

import com.hackaton.restapi.entity.Historique;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface HistoriqueRepository extends JpaRepository<Historique, Long>, JpaSpecificationExecutor<Historique> {
    
    @Query("SELECT COUNT(*) AS dose FROM Historique WHERE demandeVaccin.personne.id= ?1")
    public Integer getDose(Long id);
}
