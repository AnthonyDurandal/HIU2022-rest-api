package com.hackaton.restapi.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table
public class TestAptitude {
    @Id
    @SequenceGenerator(name = "test_aptitude_sequence", sequenceName = "test_aptitude_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "test_aptitude_sequence")
    private Long id;
    private Boolean testPositif;
    private Boolean vaccin;
    private String remarqueVaccin;
    private Boolean enceinte;
    private Boolean allaite;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Personne personne;

    @OneToMany(mappedBy = "testAptitude", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetailTestAptitude> detailTestAptitude;

    public TestAptitude() {
    }

    public TestAptitude(Long id) {
        this.id = id;
    }

    public TestAptitude(Long id, Boolean testPositif, Boolean vaccin, String remarqueVaccin, Boolean enceinte, Boolean allaite, Personne personne) {
        this.id = id;
        this.testPositif = testPositif;
        this.vaccin = vaccin;
        this.remarqueVaccin = remarqueVaccin;
        this.enceinte = enceinte;
        this.allaite = allaite;
        this.personne = personne;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isTestPositif() {
        return this.testPositif;
    }

    public Boolean getTestPositif() {
        return this.testPositif;
    }

    public void setTestPositif(Boolean testPositif) {
        this.testPositif = testPositif;
    }

    public Boolean isVaccin() {
        return this.vaccin;
    }

    public Boolean getVaccin() {
        return this.vaccin;
    }

    public void setVaccin(Boolean vaccin) {
        this.vaccin = vaccin;
    }

    public String getRemarqueVaccin() {
        return this.remarqueVaccin;
    }

    public void setRemarqueVaccin(String remarqueVaccin) {
        this.remarqueVaccin = remarqueVaccin;
    }

    public Boolean isEnceinte() {
        return this.enceinte;
    }

    public Boolean getEnceinte() {
        return this.enceinte;
    }

    public void setEnceinte(Boolean enceinte) {
        this.enceinte = enceinte;
    }

    public Boolean isAllaite() {
        return this.allaite;
    }

    public Boolean getAllaite() {
        return this.allaite;
    }

    public void setAllaite(Boolean allaite) {
        this.allaite = allaite;
    }

    public Personne getPersonne() {
        return this.personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", testPositif='" + isTestPositif() + "'" +
            ", vaccin='" + isVaccin() + "'" +
            ", remarqueVaccin='" + getRemarqueVaccin() + "'" +
            ", enceinte='" + isEnceinte() + "'" +
            ", allaite='" + isAllaite() + "'" +
            ", personne='" + getPersonne() + "'" +
            "}";
    }

}
