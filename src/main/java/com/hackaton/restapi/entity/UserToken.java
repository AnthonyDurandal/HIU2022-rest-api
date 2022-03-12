package com.hackaton.restapi.entity;

import java.sql.Timestamp;

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
public class UserToken {
    @Id
    @SequenceGenerator(name = "usertoken_sequence", sequenceName = "usertoken_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usertoken_sequence")
    private Long id;
    private String token;
    private Timestamp datePeremption;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User user;

    public UserToken() {
    }

    public UserToken(String token, Timestamp datePeremption, User user) {
        this.token = token;
        this.datePeremption = datePeremption;
        this.user = user;
    }

    public UserToken(Long id, String token, Timestamp datePeremption, User user) {
        this.id = id;
        this.token = token;
        this.datePeremption = datePeremption;
        this.user = user;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getDatePeremption() {
        return this.datePeremption;
    }

    public void setDatePeremption(Timestamp datePeremption) {
        this.datePeremption = datePeremption;
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
            ", token='" + getToken() + "'" +
            ", datePeremption='" + getDatePeremption() + "'" +
            ", user='" + getUser() + "'" +
            "}";
    }

}
