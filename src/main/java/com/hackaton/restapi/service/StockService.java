package com.hackaton.restapi.service;

import javax.transaction.Transactional;

import com.hackaton.restapi.entity.Stock;
import com.hackaton.restapi.exception.ApiRequestException;
import com.hackaton.restapi.repository.StockRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockService {
    private final StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public Stock addNewStock(Stock stock) {
        if(stock == null)
            throw new ApiRequestException("Aucune données à ajouter");
        if(stock.getCentre()==null || stock.getDateArrivee()==null || stock.getDatePeremption()==null || stock.getVaccin() == null) 
            throw new ApiRequestException("Vous devez completer tous les champs obligatoires");
        return stockRepository.save(stock);
    }

    @Transactional
    public void updateStock(Long idStock, Stock stock) {
        Stock stockModif = stockRepository.findById(idStock)
        .orElseThrow(()-> new ApiRequestException("Ce stock n'existe pas"));
        if(stock.getCentre() != null)
            stockModif.setCentre(stock.getCentre());
        if(stock.getDateArrivee() != null)
            stockModif.setDateArrivee(stock.getDateArrivee());
        if(stock.getDatePeremption() != null)
            stockModif.setDatePeremption(stock.getDatePeremption());
        if(stock.getVaccin() != null)
            stockModif.setVaccin(stock.getVaccin());
    }
}
