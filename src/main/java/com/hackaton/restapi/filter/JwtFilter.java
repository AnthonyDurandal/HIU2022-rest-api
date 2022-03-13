package com.hackaton.restapi.filter;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hackaton.restapi.entity.error.ErrorDetails;
import com.hackaton.restapi.service.CustomUserDetailsService;
import com.hackaton.restapi.util.JwtUtil;
import com.hackaton.restapi.util.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String[] url = {
            "/",
            "/api/v1/Login",
            "/api/v1/Logout",
            "/api/v1/CheckToken",
            "/api/v1/Personnes",
        };

        boolean verifUrl = true;
        for(int i=0;i<url.length;i++){
            if (request.getServletPath().compareTo(url[i]) == 0) 
                verifUrl = false;
        }

        if(verifUrl) {
            String token = request.getHeader("Authorization");
            if (token != null) {
                try {
                    String tokenWoBearer = token.substring("Bearer ".length());
                    boolean verif = jwtUtil.tokenValide(tokenWoBearer);
                    if (verif == false)
                        throw new Exception("token expiré");
                    String username = jwtUtil.extractUsername(tokenWoBearer);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails.getUsername(), null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                } catch (Exception e) {
                    ErrorDetails errorDetails = new ErrorDetails(
                            false,
                            e.getMessage(),
                            new Timestamp(System.currentTimeMillis()));
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.setContentType("application/json");
                    response.getWriter().write(Util.convertObjectToJson(errorDetails));
                    return;
                }

            } else {
                ErrorDetails errorDetails = new ErrorDetails(
                        false,
                        "Aucun token fourni",
                        new Timestamp(System.currentTimeMillis()));
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType("application/json");
                response.getWriter().write(Util.convertObjectToJson(errorDetails));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}