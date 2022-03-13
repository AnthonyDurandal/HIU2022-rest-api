package com.hackaton.restapi.repository;

import com.hackaton.restapi.entity.TestAptitude;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TestAptitudeRepository extends JpaRepository<TestAptitude, Long>, JpaSpecificationExecutor<TestAptitude> {
    
}
