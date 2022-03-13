package com.hackaton.restapi.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table
public class Stock {
    @Id
    @SequenceGenerator(name = "stock_sequence", sequenceName = "stock_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stock_sequence")
    private Long id;
    @ManyToOne
    private Vaccin vaccin;
    @ManyToOne
    private Centre centre;
    private Timestamp dateArrivee;
    private Timestamp datePeremption;
    private boolean estUtilise;

    public Stock(){}

    public Stock(Vaccin vaccin, Centre centre, Timestamp dateArrivee, Timestamp datePeremption, boolean estUtilise){
        this.vaccin = vaccin;
        this.centre = centre;
        this.dateArrivee = dateArrivee;
        this.datePeremption = datePeremption;
        this.estUtilise = estUtilise;
    }
    
    public Stock(Long id, Vaccin vaccin, Centre centre, Timestamp dateArrivee, Timestamp datePeremption, boolean estUtilise){
        this.id = id;
        this.vaccin = vaccin;
        this.centre = centre;
        this.dateArrivee = dateArrivee;
        this.datePeremption = datePeremption;
        this.estUtilise = estUtilise;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vaccin getVaccin() {
        return this.vaccin;
    }

    public void setVaccin(Vaccin vaccin) {
        this.vaccin = vaccin;
    }

    public Centre getCentre() {
        return this.centre;
    }

    public void setCentre(Centre centre) {
        this.centre = centre;
    }

    public Timestamp getDateArrivee() {
        return this.dateArrivee;
    }

    public void setDateArrivee(Timestamp dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public Timestamp getDatePeremption() {
        return this.datePeremption;
    }

    public void setDatePeremption(Timestamp datePeremption) {
        this.datePeremption = datePeremption;
    }

    public boolean isEstUtilise() {
        return this.estUtilise;
    }

    public void setEstUtilise(boolean estUtilise) {
        this.estUtilise = estUtilise;
    }

}
