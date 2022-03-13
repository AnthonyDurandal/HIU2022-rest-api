package com.hackaton.restapi.service;

import javax.transaction.Transactional;

import com.hackaton.restapi.entity.Vaccin;
import com.hackaton.restapi.exception.ApiRequestException;
import com.hackaton.restapi.repository.VaccinRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VaccinService {
    private final VaccinRepository vaccinRepository;

    @Autowired
    public VaccinService(VaccinRepository vaccinRepository) {
        this.vaccinRepository = vaccinRepository;
    }

    public Vaccin addNewVaccin(Vaccin vaccin) {
        if(vaccin==null)
            throw new ApiRequestException("Aucune données à ajouter");
        if(vaccin.getNom()==null)
            throw new ApiRequestException("Vous devez completer tous les champs obligatoires");
        return vaccinRepository.save(vaccin);
    } 

    @Transactional
    public void updateVaccin(Long vaccinId, Vaccin vaccin) {
        Vaccin vaccinModif = vaccinRepository.findById(vaccinId)
        .orElseThrow(()-> new ApiRequestException("Ce vaccin n'existe pas"));
        if(vaccin.getNom()==null)
            throw new ApiRequestException("Vous devez completer tous les champs obligatoires");
        else
            vaccinModif.setNom(vaccin.getNom());
        if(vaccin.getEcartDose() != 0)
            vaccinModif.setEcartDose(vaccin.getEcartDose());
        if(vaccin.getNombreDose() != 0)
            vaccinModif.setNombreDose(vaccin.getNombreDose());

    }
    
}
