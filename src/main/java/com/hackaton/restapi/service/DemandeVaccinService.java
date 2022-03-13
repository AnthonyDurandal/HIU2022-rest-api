package com.hackaton.restapi.service;

import javax.transaction.Transactional;

import com.hackaton.restapi.entity.DemandeVaccin;
import com.hackaton.restapi.exception.ApiRequestException;
import com.hackaton.restapi.repository.DemandeVaccinRepository;

public class DemandeVaccinService {

    private final DemandeVaccinRepository demandeVaccinRepository;

    public DemandeVaccinService(DemandeVaccinRepository demandeVaccinRepository) {
        this.demandeVaccinRepository = demandeVaccinRepository;
    }

    public DemandeVaccin addNewDemandeVaccin(DemandeVaccin demandeVaccin) {
        if(demandeVaccin == null)
            throw new ApiRequestException("Aucune données à ajouter");
        if(demandeVaccin.getCentre() == null || demandeVaccin.getDateDemande() == null || 
        demandeVaccin.getRemarque() == null || demandeVaccin.getUser() == null || 
        demandeVaccin.getVaccin() == null)
            throw new ApiRequestException("Vous devez completer tous les champs obligatoires");
        return demandeVaccinRepository.save(demandeVaccin);
    }

    @Transactional
    public void updateDemandeVaccin(Long id, DemandeVaccin demandeVaccin) {
        DemandeVaccin demandeVaccinModif = demandeVaccinRepository.findById(id)
        .orElseThrow(()-> new ApiRequestException("Cette demande vaccin n'existe pas"));
        if(demandeVaccin.getCentre() != null)
            demandeVaccinModif.setCentre(demandeVaccin.getCentre());
        if(demandeVaccin.getDateDemande() != null)
            demandeVaccinModif.setDateDemande(demandeVaccin.getDateDemande());
        if(demandeVaccin.getRemarque() != null)
            demandeVaccinModif.setRemarque(demandeVaccin.getRemarque());
        if(demandeVaccin.getUser() != null)
            demandeVaccinModif.setUser(demandeVaccin.getUser());
        if(demandeVaccin.getVaccin() != null)
            demandeVaccinModif.setVaccin(demandeVaccin.getVaccin());
    }
}
