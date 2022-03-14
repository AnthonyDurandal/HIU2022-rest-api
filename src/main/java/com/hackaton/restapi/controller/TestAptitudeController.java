package com.hackaton.restapi.controller;

import com.hackaton.restapi.entity.TestAptitude;
import com.hackaton.restapi.service.TestAptitudeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/TestAptitudes")
public class TestAptitudeController {

    private final TestAptitudeService testAptitudeService;

    @Autowired    
    public TestAptitudeController(TestAptitudeService testAptitudeService) {
        this.testAptitudeService = testAptitudeService;
    }

    @PostMapping
    public TestAptitude addNewTestAptitudeController(@RequestBody TestAptitude testAptitude) throws Exception {
        return testAptitudeService.addNewTestAptitude(testAptitude);
    }
}