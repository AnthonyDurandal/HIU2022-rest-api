package com.hackaton.restapi.service;

import java.sql.Timestamp;

import com.hackaton.restapi.entity.Personne;
import com.hackaton.restapi.entity.User;
import com.hackaton.restapi.exception.ApiRequestException;
import com.hackaton.restapi.repository.PersonneRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonneService {
    private  PersonneRepository personneRepository;
    private  UserService userService;

    @Autowired
    public PersonneService(PersonneRepository personneRepository, UserService userService) {
        this.personneRepository = personneRepository;
        this.userService = userService;
    }

    public Personne addNewPersonne(Personne personne) {
        if (personne == null)
            throw new ApiRequestException("Aucune données à ajouter");
        if (personne.getNom() == null ||
                personne.getPrenom() == null ||
                personne.getDateDeNaissance() == null ||
                personne.getCin() == null ||
                personne.getMail() == null ||
                personne.getUser() == null) {
            throw new ApiRequestException("Vous devez completer tous les champs obligatoires");
        }
        if(personne.getDateDeNaissance().getTime() >= new Timestamp(System.currentTimeMillis()).getTime()){
            throw new ApiRequestException("Date de naissance ne peut pas être au dela de ce jour");
        }
        if(personne.getCin().length() != 12) {
            throw new ApiRequestException("Numéro de CIN non valide");
        }
        if (personne.getCin().length() != 12) {
            throw new ApiRequestException("Numéro de CIN non valide");
        }
        String regexMail = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        if (!personne.getMail().matches(regexMail)) {
            throw new ApiRequestException("Format de mail invalide");
        }
        
        User user = userService.addNewUser(personne.getUser(), "personne");
        personne.setUser(user);
        return personneRepository.save(personne);
    }
}
