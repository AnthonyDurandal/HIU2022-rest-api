package com.hackaton.restapi.repository;

import com.hackaton.restapi.entity.Centre;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CentreRepository extends JpaRepository<Centre, Long>,
        JpaSpecificationExecutor<Centre> {
}