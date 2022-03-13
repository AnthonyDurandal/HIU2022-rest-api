package com.hackaton.restapi.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table
public class Vaccin {
    @Id
    @SequenceGenerator(name = "vaccin_sequence", sequenceName = "vaccin_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vaccin_sequence")
    private Long id;
    private String nom;
    private int nombreDose;
    private int ecartDose;

    public Vaccin(){}

    public Vaccin(String nom, int nombreDose, int ecartDose){
        this.nom = nom;
        this.nombreDose = nombreDose;
        this.ecartDose = ecartDose;
    }

    public Vaccin(Long id, String nom, int nombreDose, int ecartDose){
        this.id = id;
        this.nom = nom;
        this.nombreDose = nombreDose;
        this.ecartDose = ecartDose;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNombreDose() {
        return this.nombreDose;
    }

    public void setNombreDose(int nombreDose) {
        this.nombreDose = nombreDose;
    }

    public int getEcartDose() {
        return this.ecartDose;
    }

    public void setEcartDose(int ecartDose) {
        this.ecartDose = ecartDose;
    }

}
