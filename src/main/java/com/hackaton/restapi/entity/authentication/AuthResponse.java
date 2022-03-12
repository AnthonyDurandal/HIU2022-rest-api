package com.hackaton.restapi.entity.authentication;

import com.hackaton.restapi.entity.User;

public class AuthResponse {

    private final User user;
    private final String jwtToken;

    public AuthResponse(User user, String jwtToken) {
        this.user = user;
        this.jwtToken = jwtToken;
    }

    public User getUser() {
        return this.user;
    }

    public String getToken() {
        return this.jwtToken;
    }
}
