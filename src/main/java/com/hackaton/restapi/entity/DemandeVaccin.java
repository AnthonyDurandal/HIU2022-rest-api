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
public class DemandeVaccin {
    @Id
    @SequenceGenerator(name = "demande_vaccin_sequence", sequenceName = "demande_vaccin_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "demande_vaccin_sequence")
    private Long id;
    private Timestamp dateDemande;
    @ManyToOne
    private Personne personne;
    @ManyToOne
    private Centre centre;
    @ManyToOne
    private Vaccin vaccin;
    private Boolean estAnnule;
    private String remarque;
    
    public DemandeVaccin() {
    }

    public DemandeVaccin(Timestamp dateDemande, Personne personne, Centre centre, Vaccin vaccin, Boolean estAnnule,
            String remarque) {
        this.dateDemande = dateDemande;
        this.personne = personne;
        this.centre = centre;
        this.vaccin = vaccin;
        this.estAnnule = estAnnule;
        this.remarque = remarque;
    }

    public DemandeVaccin(Long id, Timestamp dateDemande, Personne personne, Centre centre, Vaccin vaccin, Boolean estAnnule,
            String remarque) {
        this.id = id;
        this.dateDemande = dateDemande;
        this.personne = personne;
        this.centre = centre;
        this.vaccin = vaccin;
        this.estAnnule = estAnnule;
        this.remarque = remarque;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getDateDemande() {
        return this.dateDemande;
    }

    public void setDateDemande(Timestamp dateDemande) {
        this.dateDemande = dateDemande;
    }

    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    public Centre getCentre() {
        return this.centre;
    }

    public void setCentre(Centre centre) {
        this.centre = centre;
    }

    public Vaccin getVaccin() {
        return this.vaccin;
    }

    public void setVaccin(Vaccin vaccin) {
        this.vaccin = vaccin;
    }

    public Boolean isEstAnnule() {
        return this.estAnnule;
    }

    public void setEstAnnule(Boolean estAnnule) {
        this.estAnnule = estAnnule;
    }

    public String getRemarque() {
        return this.remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

}
