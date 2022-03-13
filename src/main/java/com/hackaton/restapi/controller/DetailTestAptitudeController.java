package com.hackaton.restapi.controller;

import com.hackaton.restapi.entity.DetailTestAptitude;
import com.hackaton.restapi.service.DetailTestAptitudeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/DetailTestAptitudes")
public class DetailTestAptitudeController {

    private final DetailTestAptitudeService detailTestAptitudeService;

    @Autowired
    public DetailTestAptitudeController(DetailTestAptitudeService detailTestAptitudeService) {
        this.detailTestAptitudeService = detailTestAptitudeService;
    }

    @PostMapping
    public DetailTestAptitude addNewDetailTestAptitudeController(@RequestBody DetailTestAptitude detailTestAptitude) throws Exception {
        return detailTestAptitudeService.addNewDetailTestAptitude(detailTestAptitude);
    }
}
