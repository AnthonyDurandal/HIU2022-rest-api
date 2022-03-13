package com.hackaton.restapi.entity;

import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table
public class Centre {
    @Id
    @SequenceGenerator(name = "centre_sequence", sequenceName = "centre_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "centre_sequence")
    private Long id;
    private String nom;
    private Double longitude;
    private Double latitude;
    private Boolean estOuvert;
    private LocalTime ouverture;
    private LocalTime fermeture;
    private Integer nombrePersonnel;
    @OneToOne
    private User user;

    public Centre() {
    }

    public Centre(String nom, Double longitude, Double latitude, Boolean estOuvert, LocalTime ouverture,
            LocalTime fermeture, Integer nombrePersonnel, User user) {
        this.nom = nom;
        this.longitude = longitude;
        this.latitude = latitude;
        this.estOuvert = estOuvert;
        this.ouverture = ouverture;
        this.fermeture = fermeture;
        this.nombrePersonnel = nombrePersonnel;
        this.user = user;
    }

    public Centre(Long id) {
        this.id = id;
    }

    public Centre(Long id, String nom, Double longitude, Double latitude, Boolean estOuvert, LocalTime ouverture,
            LocalTime fermeture, Integer nombrePersonnel, User user) {
        this.id = id;
        this.nom = nom;
        this.longitude = longitude;
        this.latitude = latitude;
        this.estOuvert = estOuvert;
        this.ouverture = ouverture;
        this.fermeture = fermeture;
        this.nombrePersonnel = nombrePersonnel;
        this.user = user;
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

    public Double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Boolean getEstOuvert() {
        return this.estOuvert;
    }

    public void setEstOuvert(Boolean estOuvert) {
        this.estOuvert = estOuvert;
    }

    public LocalTime getOuverture() {
        return this.ouverture;
    }

    public void setOuverture(LocalTime ouverture) {
        this.ouverture = ouverture;
    }

    public LocalTime getFermeture() {
        return this.fermeture;
    }

    public void setFermeture(LocalTime fermeture) {
        this.fermeture = fermeture;
    }

    public Integer getNombrePersonnel() {
        return this.nombrePersonnel;
    }

    public void setNombrePersonnel(Integer nombrePersonnel) {
        this.nombrePersonnel = nombrePersonnel;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
