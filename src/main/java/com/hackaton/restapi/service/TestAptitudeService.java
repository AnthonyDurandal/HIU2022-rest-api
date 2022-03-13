package com.hackaton.restapi.service;

import javax.transaction.Transactional;

import com.hackaton.restapi.entity.TestAptitude;
import com.hackaton.restapi.exception.ApiRequestException;
import com.hackaton.restapi.repository.TestAptitudeRepository;

import org.springframework.stereotype.Service;

@Service
public class TestAptitudeService {
    
    private final TestAptitudeRepository testAptitudeRepository;

    public TestAptitudeService(TestAptitudeRepository testAptitudeRepository) {
        this.testAptitudeRepository = testAptitudeRepository;
    }

    public TestAptitude addNewTestAptitude(TestAptitude testAptitude) {
        if(testAptitude == null)
            throw new ApiRequestException("Aucune données à ajouter");
        if(testAptitude.getAllaite() == null || testAptitude.getEnceinte() == null || testAptitude.getTestPositif() == null || 
        testAptitude.getVaccin() == null || testAptitude.getPersonne() == null)
            throw new ApiRequestException("Vous devez completer tous les champs obligatoires");
        if(testAptitude.getAllaite() || testAptitude.getEnceinte() || testAptitude.getTestPositif() || testAptitude.getVaccin())
            throw new ApiRequestException("Vous ne pouvez pas faire le vaccin.");
        return testAptitudeRepository.save(testAptitude);
    }

    @Transactional
    public void updateTestAptitude(Long id, TestAptitude testAptitude) {
        TestAptitude testAptitudeModif = testAptitudeRepository.findById(id)
        .orElseThrow(()-> new ApiRequestException("Ce test aptitude n'existe pas"));
        if(testAptitude.getAllaite() != null)
            testAptitudeModif.setAllaite(testAptitude.getAllaite());
        if(testAptitude.getEnceinte() != null)
            testAptitudeModif.setEnceinte(testAptitude.getEnceinte());
        if(testAptitude.getTestPositif() != null)
            testAptitudeModif.setTestPositif(testAptitude.getTestPositif());
        if(testAptitude.getVaccin() != null)
            testAptitudeModif.setVaccin(testAptitude.getVaccin());
        if(testAptitude.getPersonne() != null)
            testAptitudeModif.setPersonne(testAptitude.getPersonne());
        if(testAptitude.getRemarqueVaccin() != null) 
            testAptitudeModif.setRemarqueVaccin(testAptitude.getRemarqueVaccin());
    }
}
