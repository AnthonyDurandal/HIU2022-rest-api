package com.hackaton.restapi.service;

import javax.transaction.Transactional;

import com.hackaton.restapi.entity.DetailTestAptitude;
import com.hackaton.restapi.exception.ApiRequestException;
import com.hackaton.restapi.repository.DetailTestAptitudeRepository;

import org.springframework.stereotype.Service;

@Service
public class DetailTestAptitudeService {

    private final DetailTestAptitudeRepository detailTestAptitudeRepository;

    public DetailTestAptitudeService(DetailTestAptitudeRepository detailTestAptitudeRepository) {
        this.detailTestAptitudeRepository = detailTestAptitudeRepository;
    }

    public DetailTestAptitude addNewDetailTestAptitude(DetailTestAptitude detailTestAptitude) {
        if(detailTestAptitude == null)
            throw new ApiRequestException("Aucune données à ajouter");
        if(detailTestAptitude.getMaladie() == null || detailTestAptitude.getTestAptitude() == null)
            throw new ApiRequestException("Vous devez completer tous les champs obligatoires");
        return detailTestAptitudeRepository.save(detailTestAptitude);
    }

    @Transactional
    public void updateDetailTestAptitude(Long id, DetailTestAptitude detailTestAptitude){
        DetailTestAptitude detailTestAptitudeModif = detailTestAptitudeRepository.findById(id)
        .orElseThrow(()-> new ApiRequestException("Ce detail test aptitude n'existe pas"));
        if(detailTestAptitude.getMaladie() != null)
            detailTestAptitudeModif.setMaladie(detailTestAptitude.getMaladie());
        if(detailTestAptitude.getTestAptitude() != null)
            detailTestAptitudeModif.setTestAptitude(detailTestAptitude.getTestAptitude());
    }
}
