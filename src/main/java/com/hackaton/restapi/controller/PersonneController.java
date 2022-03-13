package com.hackaton.restapi.controller;

import com.hackaton.restapi.entity.Personne;
import com.hackaton.restapi.entity.response.ResponseContent;
import com.hackaton.restapi.service.PersonneService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1")
public class PersonneController {
    private final PersonneService personneService;

    @Autowired
    public PersonneController(PersonneService personneService){
        this.personneService =personneService;
    }

    @PostMapping(path = "/Personnes")
    public ResponseContent registerNewPersonne(@RequestBody Personne personne) {
        Personne personneResponse = personneService.addNewPersonne(personne);
        return new ResponseContent(
                true,
                "Nouvelle Personne ajoutée",
                personneResponse,
                null,
                null);
    }
}
