package com.hackaton.restapi.controller;

import com.hackaton.restapi.service.StockService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1")
public class StockController {
    
    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService){
        this.stockService =stockService;
    }
}
