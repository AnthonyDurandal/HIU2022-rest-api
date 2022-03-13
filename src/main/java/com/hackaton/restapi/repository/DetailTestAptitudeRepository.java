package com.hackaton.restapi.repository;

import com.hackaton.restapi.entity.DetailTestAptitude;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DetailTestAptitudeRepository extends JpaRepository<DetailTestAptitude, Long>, JpaSpecificationExecutor<DetailTestAptitude> {
    
}
