package com.hackaton.restapi.config;

import java.util.Optional;

import com.hackaton.restapi.entity.User;
import com.hackaton.restapi.repository.UserRepository;
import com.hackaton.restapi.util.CustomUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@EnableWebSecurity
public class CustomAuthenticationManager implements AuthenticationManager {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getPrincipal() + "";
        String password = authentication.getCredentials() + "";
        String role = authentication.getAuthorities().toString();
        role = role.substring(1, role.length() - 1);
        Optional<User> user = userRepository.findByUsernameAndRole(username, role);
        if (!user.isPresent()) {
            throw new BadCredentialsException("Username ou mot de passe incorrect");
        }
        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            throw new BadCredentialsException("Username ou mot de passe incorrect");
        }
        CustomUserDetails userDet = new CustomUserDetails(user.get());

        return new UsernamePasswordAuthenticationToken(
                username,
                password,
                userDet.getAuthorities());
    }

}