package com.hackaton.restapi.controller;

import com.hackaton.restapi.entity.DemandeVaccin;
import com.hackaton.restapi.service.DemandeVaccinService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/DemandeVaccins")
public class DemandeVaccinController {
    
    private final DemandeVaccinService demandeVaccinService;

    @Autowired
    public DemandeVaccinController(DemandeVaccinService demandeVaccinService) {
        this.demandeVaccinService = demandeVaccinService;
    }

    @PostMapping
    public DemandeVaccin adddemandeVaccin(@RequestBody DemandeVaccin demandeVaccin) {
        return demandeVaccinService.addNewDemandeVaccin(demandeVaccin);
    }
}
