package com.hackaton.restapi.controller;

import com.hackaton.restapi.entity.Centre;
import com.hackaton.restapi.entity.response.ResponseContent;
import com.hackaton.restapi.service.CentreService;
import com.hackaton.restapi.util.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1")
public class CentreController {
    private final CentreService centreService;

    @Autowired
    public CentreController(CentreService centreService) {
        this.centreService = centreService;
    }

    @PostMapping(path = "/Centres")
    public ResponseContent registerNewCentre(@RequestBody Centre centre) {
        Centre centreResponse = centreService.addNewCentre(centre);
        return new ResponseContent(
                true,
                "Nouveau centre ajouté",
                centreResponse,
                null,
                null);
    }

    @GetMapping(path = "/Centres")
    public ResponseContent findCentre(
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "page-num", required = false) Integer page,
            @RequestParam(name = "page-size", required = false) Integer size,
            @RequestParam(required = false) String id, 
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String expressionLongitude, 
            @RequestParam(required = false) String expressionLatitude, 
            @RequestParam(required = false) String expressionOuverture, 
            @RequestParam(required = false) String expressionFermeture,
            @RequestParam(required = false) String nombrePersonnel,
            @RequestParam(required = false) String idUser)
        {
        //getCentre(String sort, Integer page, Integer size,String id, String nom,String expressionLongitude, String expressionLatitude, StrexpressionOuverture, String expressionFermeture,String nombrePersonnel,String idUser)
        Page<Centre> data = centreService.getCentre( sort,  page,  size, id,  nom, expressionLongitude,  expressionLatitude, expressionOuverture,  expressionFermeture, nombrePersonnel, idUser);
        int pagePlusUn = data.getNumber() + 1;
        // Centre centreResponse = centreService.addNewCentre(centre);
        return new ResponseContent(
                true,
                "Liste des centres de vaccination",
                data.getContent(),
                Util.metaData(data.getTotalElements(), data.getTotalPages(), pagePlusUn,
                        data.getSize()),
                Util.links(sort, data.getTotalPages(), pagePlusUn, data.getSize()));
    }
}
