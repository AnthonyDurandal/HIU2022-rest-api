package com.hackaton.restapi.service;

import java.util.Optional;

import com.hackaton.restapi.entity.User;
import com.hackaton.restapi.exception.ApiRequestException;
import com.hackaton.restapi.repository.UserRepository;
import com.hackaton.restapi.util.CustomUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new ApiRequestException("Username ou mot de passe incorrect");
        }
        User user = userOptional.get();
        return new CustomUserDetails(user);
    }

}