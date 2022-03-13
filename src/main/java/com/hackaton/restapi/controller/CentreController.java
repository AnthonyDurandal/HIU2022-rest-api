package com.hackaton.restapi.controller;

import com.hackaton.restapi.entity.Centre;
import com.hackaton.restapi.entity.response.ResponseContent;
import com.hackaton.restapi.service.CentreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
