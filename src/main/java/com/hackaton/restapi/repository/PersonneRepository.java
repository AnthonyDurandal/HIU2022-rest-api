package com.hackaton.restapi.repository;

import com.hackaton.restapi.entity.Personne;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PersonneRepository extends JpaRepository<Personne, Long>,
        JpaSpecificationExecutor<Personne> {

}