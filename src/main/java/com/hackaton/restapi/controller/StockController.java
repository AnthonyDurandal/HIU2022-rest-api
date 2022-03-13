package com.hackaton.restapi.controller;

import com.hackaton.restapi.entity.Stock;
import com.hackaton.restapi.entity.response.ResponseContent;
import com.hackaton.restapi.service.StockService;
import com.hackaton.restapi.util.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/Stock")
public class StockController {
    
    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService){
        this.stockService =stockService;
    }

    @GetMapping(path = "")
    public ResponseContent findStock(
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "page-num", required = false) Integer page,
            @RequestParam(name = "page-size", required = false) Integer size,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String expressionVaccin,
            @RequestParam(required = false) String expressionCentre,
            @RequestParam(required = false) String expressionDateArrivee,
            @RequestParam(required = false) String expressionDatePeremtion
        ) {
        // getStock(String sort, Integer page, Integer size,
        //     String id, String expressionVaccin,
        //     String expressionCentre, String expressionDateArrivee, String expressionDatePeremtion)
        Page<Stock> data = stockService.getStock( sort,  page,  size,
             id,  expressionVaccin,
             expressionCentre,  expressionDateArrivee,  expressionDatePeremtion);
        int pagePlusUn = data.getNumber() + 1;
        // Centre centreResponse = centreService.addNewCentre(centre);
        return new ResponseContent(
                true,
                "Liste : stock",
                data.getContent(),
                Util.metaData(data.getTotalElements(), data.getTotalPages(), pagePlusUn,
                        data.getSize()),
                Util.links(sort, data.getTotalPages(), pagePlusUn, data.getSize()));
    }
}
