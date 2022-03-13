package com.hackaton.restapi.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table
public class Maladie {
    @Id
    @SequenceGenerator(name = "maladie_sequence", sequenceName = "maladie_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "maladie_sequence")
    private Long id;
    private String nom;

    @OneToMany(mappedBy = "maladie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetailTestAptitude> detailTestAptitude;

    public Maladie() {
    }

    public Maladie(String nom) {
        this.nom = nom;
    }

    public Maladie(Long id) {
        this.id = id;
    }

    public Maladie(Long id, String nom) {
        this.id = id;
        this.nom = nom;
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

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", nom='" + getNom() + "'" +
            "}";
    }

}
