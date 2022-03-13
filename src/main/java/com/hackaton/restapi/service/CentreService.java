package com.hackaton.restapi.service;

import com.hackaton.restapi.entity.Centre;
import com.hackaton.restapi.entity.User;
import com.hackaton.restapi.exception.ApiRequestException;
import com.hackaton.restapi.repository.CentreRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CentreService {
    private CentreRepository centreRepository;
    private UserService userService;

    @Autowired
    public CentreService(CentreRepository centreRepository, UserService userService) {
        this.centreRepository = centreRepository;
        this.userService = userService;
    }

    public Centre addNewCentre(Centre centre) {
        if (centre == null)
            throw new ApiRequestException("Aucune données à ajouter");
        
        if(centre.getNom() == null ||
            centre.getLongitude() == null ||
            centre.getLatitude() == null ||
            centre.getEstOuvert() == null ||
            centre.getOuverture() == null ||
            centre.getFermeture() == null ||
            centre.getNombrePersonnel() == null ||
            centre.getUser() == null){
            throw new ApiRequestException("Vous devez completer tous les champs obligatoires");
        }

        User user = userService.addNewUser(centre.getUser(), "centreVaccination");
        centre.setUser(user);
        return centreRepository.save(centre);
    }
}
