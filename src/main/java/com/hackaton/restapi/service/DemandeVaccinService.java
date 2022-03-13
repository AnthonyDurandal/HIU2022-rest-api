package com.hackaton.restapi.service;

import javax.transaction.Transactional;

import com.hackaton.restapi.entity.DemandeVaccin;
import com.hackaton.restapi.exception.ApiRequestException;
import com.hackaton.restapi.repository.DemandeVaccinRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemandeVaccinService {

    private final DemandeVaccinRepository demandeVaccinRepository;
    private final CentreService centreService;

    @Autowired
    public DemandeVaccinService(DemandeVaccinRepository demandeVaccinRepository, CentreService centreService) {
        this.demandeVaccinRepository = demandeVaccinRepository;
        this.centreService = centreService;
    }

    public DemandeVaccin addNewDemandeVaccin(DemandeVaccin demandeVaccin) {
        if(demandeVaccin == null)
            throw new ApiRequestException("Aucune données à ajouter");
        if(demandeVaccin.getCentre() == null || demandeVaccin.getDateDemande() == null || 
        demandeVaccin.getRemarque() == null || demandeVaccin.getPersonne() == null || 
        demandeVaccin.getVaccin() == null || demandeVaccin.isEstAnnule() == null)
            throw new ApiRequestException("Vous devez completer tous les champs obligatoires");
        if(!centreService.findById(demandeVaccin.getCentre().getId()).getEstOuvert())
            throw new ApiRequestException("Vous devez choisir un centre ouvert");    
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
        if(demandeVaccin.getPersonne() != null)
            demandeVaccinModif.setPersonne(demandeVaccin.getPersonne());
        if(demandeVaccin.getVaccin() != null)
            demandeVaccinModif.setVaccin(demandeVaccin.getVaccin());
        if(demandeVaccin.isEstAnnule() != null)
            demandeVaccinModif.setEstAnnule(demandeVaccin.isEstAnnule());
    }
}
