package com.hackaton.restapi.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table
public class Historique {
    @Id
    @SequenceGenerator(name = "historique_sequence", sequenceName = "historique_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "historique_sequence")
    private Long id; 
    @OneToOne
    private DemandeVaccin demandeVaccin;
    
    public Historique() {
    }

    public Historique(DemandeVaccin demandeVaccin) {
        this.demandeVaccin = demandeVaccin;
    }

    public Historique(Long id, DemandeVaccin demandeVaccin) {
        this.id = id;
        this.demandeVaccin = demandeVaccin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DemandeVaccin getDemandeVaccin() {
        return demandeVaccin;
    }

    public void setDemandeVaccin(DemandeVaccin demandeVaccin) {
        this.demandeVaccin = demandeVaccin;
    }
    
}
