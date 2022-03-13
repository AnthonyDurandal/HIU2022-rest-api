package com.hackaton.restapi.service;

import javax.transaction.Transactional;

import com.hackaton.restapi.entity.Maladie;
import com.hackaton.restapi.exception.ApiRequestException;
import com.hackaton.restapi.repository.MaladieRepository;

import org.springframework.stereotype.Service;

@Service
public class MaladieService {
    
    private final MaladieRepository maladieRepository;

    public MaladieService(MaladieRepository maladieRepository) {
        this.maladieRepository = maladieRepository;
    }

    public Maladie addNewMaladie(Maladie maladie) {
        if(maladie == null)
            throw new ApiRequestException("Aucune données à ajouter");
        if(maladie.getNom() == null)
            throw new ApiRequestException("Vous devez completer tous les champs obligatoires");
        return maladieRepository.save(maladie);
    }

    @Transactional
    public void updateMaladie(Long id, Maladie maladie) {
        Maladie maladieModif = maladieRepository.findById(id)
        .orElseThrow(()-> new ApiRequestException("Ce detail test aptitude n'existe pas"));
        if(maladie.getNom() != null)
            maladieModif.setNom(maladie.getNom());
    }
}
