package com.hackaton.restapi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hackaton.restapi.config.CustomAuthenticationManager;
import com.hackaton.restapi.entity.User;
import com.hackaton.restapi.entity.authentication.AuthRequest;
import com.hackaton.restapi.entity.authentication.AuthResponse;
import com.hackaton.restapi.entity.response.ResponseContent;
import com.hackaton.restapi.service.UserService;
import com.hackaton.restapi.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class WelcomeController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomAuthenticationManager authenticationManager;

    @Autowired
    private UserService user;

    @GetMapping
    public String welcome() {
        return "Welcome to Hackaton Rest API";
    }

    @PostMapping(path = "api/v1/Login")
    public AuthResponse loginWeb(@RequestBody AuthRequest authRequest) throws Exception {
        String token = null;
        try {
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("admin"));
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword(),
                            authorities));
            token = jwtUtil.generateToken(authRequest.getUsername());
        } catch (BadCredentialsException e) {
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("user"));
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword(),
                            authorities));
            token = jwtUtil.generateToken(authRequest.getUsername());
        }
        jwtUtil.enregistrerToken(token);
        return new AuthResponse(user.getUserByUsername(authRequest.getUsername()).get(), token);
    }

    @PostMapping(path = "/api/v1/Logout")
    public ResponseContent deconnexion(@RequestBody User userDeco) throws Exception {
        user.deconnexion(userDeco);
        return new ResponseContent(
                true,
                "User deconnecté",
                null,
                null,
                null);
    }

    @PostMapping(path = "/api/v1/CheckToken")
    public ResponseContent checkToken(@RequestBody HashMap<String, String> token) throws Exception {
        String tokenStr = token.get("token");
        boolean verif = jwtUtil.tokenValide(tokenStr);
        if (verif == false)
            throw new Exception("token expiré");
        return new ResponseContent(
                true,
                "Token valide",
                null,
                null,
                null);
    }
}