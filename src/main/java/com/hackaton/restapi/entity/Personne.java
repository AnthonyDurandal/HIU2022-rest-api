package com.hackaton.restapi.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table
public class Personne {
    @Id
    @SequenceGenerator(name = "personne_sequence", sequenceName = "personne_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personne_sequence")
    private Long id;
    private String nom;
    private String prenom;
    private Timestamp dateDeNaissance;
    private String cin;
    private String mail;
    
    @OneToOne
    private User user;

    @OneToMany(mappedBy = "personne", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestAptitude> testAptitude;

    public Personne() {
    }

    public Personne(Long id, String nom, String prenom, Timestamp dateDeNaissance, String cin, String mail, User user) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateDeNaissance = dateDeNaissance;
        this.cin = cin;
        this.mail = mail;
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

    public String getPrenom() {
        return this.prenom;
    }

    public void setPrennom(String prennom) {
        this.prenom = prennom;
    }

    public Timestamp getDateDeNaissance() {
        return this.dateDeNaissance;
    }

    public void setDateDeNaissance(Timestamp dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public String getCin() {
        return this.cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getMail() {
        return this.mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", nom='" + getNom() + "'" +
            ", prennom='" + getPrenom() + "'" +
            ", dateDeNaissance='" + getDateDeNaissance() + "'" +
            ", cin='" + getCin() + "'" +
            ", mail='" + getMail() + "'" +
            ", user='" + getUser() + "'" +
            "}";
    }

}
