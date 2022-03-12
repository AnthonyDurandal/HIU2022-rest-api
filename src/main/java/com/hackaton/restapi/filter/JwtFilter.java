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

        if (request.getServletPath().compareTo("/") == 0) {

        } else if (request.getServletPath().compareTo("/api/v1/Login") == 0) {

        } else if (request.getServletPath().compareTo("/api/v1/Logout") == 0) {

        } else if (request.getServletPath().compareTo("/api/v1/CheckToken") == 0) {

        } else {
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