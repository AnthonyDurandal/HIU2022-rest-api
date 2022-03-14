package com.hackaton.restapi.controller;

import java.util.HashMap;

import com.hackaton.restapi.entity.Personne;
import com.hackaton.restapi.entity.response.ResponseContent;
import com.hackaton.restapi.service.PersonneService;
import com.hackaton.restapi.util.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/Personnes/{id}/dose")
    public HashMap<String, Integer> dose(@PathVariable Long id) {
        HashMap<String, Integer> result = new HashMap<>();
        result.put("dose", personneService.getDose(id));
        return result;
    }

    @GetMapping(path = "/Personnes")
    public ResponseContent findCentre(
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "page-num", required = false) Integer page,
            @RequestParam(name = "page-size", required = false) Integer size,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String expressionNom,
            @RequestParam(required = false) String expressionPrenom,
            @RequestParam(required = false) String expressionDateNaissance,
            @RequestParam(required = false) String expressionCIN,
            @RequestParam(required = false) String expressionMail,
            @RequestParam(required = false) String expressionIdUser)
        {
        // getPersonne(String sort, Integer page, Integer size,
        //     String id, String expressionNom,
        //     String expressionPrenom, String expressionDateNaissance, String expressionCIN,String expressionMail,String expressionIdUser)
        Page<Personne> data = personneService.getPersonne( sort,  page,  size,
             id, expressionNom, expressionPrenom,  expressionDateNaissance,  expressionCIN, expressionMail,  expressionIdUser);
        int pagePlusUn = data.getNumber() + 1;
        // Centre centreResponse = centreService.addNewCentre(centre);
        return new ResponseContent(
                true,
                "Liste des personnes",
                data.getContent(),
                Util.metaData(data.getTotalElements(), data.getTotalPages(), pagePlusUn,
                        data.getSize()),
                Util.links(sort, data.getTotalPages(), pagePlusUn, data.getSize()));
    }
}
