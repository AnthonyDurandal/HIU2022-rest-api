package com.hackaton.restapi.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table
public class DetailTestAptitude {
    @Id
    @SequenceGenerator(name = "detail_test_aptitude_sequence", sequenceName = "detail_test_aptitude_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detail_test_aptitude_sequence")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private TestAptitude testAptitude;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Maladie maladie;


    public DetailTestAptitude() {
    }

    public DetailTestAptitude(Long id) {
        this.id = id;
    }

    public DetailTestAptitude(Long id, TestAptitude testAptitude, Maladie maladie) {
        this.id = id;
        this.testAptitude = testAptitude;
        this.maladie = maladie;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TestAptitude getTestAptitude() {
        return this.testAptitude;
    }

    public void setTestAptitude(TestAptitude testAptitude) {
        this.testAptitude = testAptitude;
    }

    public Maladie getMaladie() {
        return this.maladie;
    }

    public void setMaladie(Maladie maladie) {
        this.maladie = maladie;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", testAptitude='" + getTestAptitude() + "'" +
            ", maladie='" + getMaladie() + "'" +
            "}";
    }

}
