package com.hackaton.restapi.repository;

import com.hackaton.restapi.entity.Maladie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MaladieRepository extends JpaRepository<Maladie, Long>, JpaSpecificationExecutor<Maladie> {
    
}
