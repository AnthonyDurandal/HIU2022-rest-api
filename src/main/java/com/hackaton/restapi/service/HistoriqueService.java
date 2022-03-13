package com.hackaton.restapi.service;

import javax.transaction.Transactional;

import com.hackaton.restapi.entity.Historique;
import com.hackaton.restapi.exception.ApiRequestException;
import com.hackaton.restapi.repository.HistoriqueRepository;

import org.springframework.stereotype.Service;

@Service
public class HistoriqueService {
    
    private final HistoriqueRepository historiqueRepository;

    public HistoriqueService(HistoriqueRepository historiqueRepository) {
        this.historiqueRepository = historiqueRepository;
    }

    public Historique addNewHistorique(Historique historique) {
        if(historique == null)
            throw new ApiRequestException("Aucune données à ajouter");
        if(historique.getDemandeVaccin() == null)
               throw new ApiRequestException("Vous devez completer tous les champs obligatoires");
        return historiqueRepository.save(historique);
    }

    @Transactional
    public void updateHistorique(Long id, Historique historique) {
        Historique historiqueModif = historiqueRepository.findById(id)
        .orElseThrow(()-> new ApiRequestException("Cet historique n'existe pas"));
        if(historique.getDemandeVaccin() != null)
            historiqueModif.setDemandeVaccin(historique.getDemandeVaccin());
    }
    
}
