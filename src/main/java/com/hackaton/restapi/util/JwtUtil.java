package com.hackaton.restapi.util;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import com.hackaton.restapi.entity.User;
import com.hackaton.restapi.entity.UserToken;
import com.hackaton.restapi.repository.UserRepository;
import com.hackaton.restapi.repository.UserTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;

@Service
public class JwtUtil {

    @Autowired
    private UserTokenRepository userTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public static final long JWT_TOKEN_VALIDITY = 12 * 60 * 60;

    @Value("${jwt.secret}")
    private String secret;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
        .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    public void enregistrerToken(String token) {
        Long datePerempMillis = System.currentTimeMillis() + (JWT_TOKEN_VALIDITY * 1000);
        String username = extractUsername(token);
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional.get();
        UserToken userToken = new UserToken(token, new Timestamp(datePerempMillis), user);
        userTokenRepository.save(userToken);
    }

    public boolean tokenValide(String token) throws Exception {
        boolean res = true;
        Optional<UserToken> uOptional = userTokenRepository.findByToken(token);
        if (!uOptional.isPresent()) {
            throw new Exception("Token non valide");
        }
        UserToken userToken = uOptional.get();
        if (userToken.getDatePeremption().before(new Date())) {
            res = false;
        }
        return res;
    }

    // public boolean autorisationUser(Authentication authentication, Long userId) {
    //     UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
    //     String role = auth.getAuthorities().toString();
    //     role = role.substring(1, role.length() - 1);
    //     String username = (String) auth.getPrincipal();
    //     Optional<User> userOptional = userRepository.findByUsername(username);
    //     User user = userOptional.get();
    //     boolean res = false;
    //     if (user.getId() == userId) {
    //         res = true;
    //     }
    //     if (role.compareTo("admin") == 0)
    //         res = true;
    //     return res;
    // }

    // public Boolean validateToken(String token, UserDetails userDetails) {
    // final String username = extractUsername(token);
    // return (username.equals(userDetails.getUsername()) &&
    // !isTokenExpired(token));
    // }

    // public Date extractExpiration(String token) {
    // return extractClaim(token, Claims::getExpiration);
    // }

    // private Boolean isTokenExpired(String token) {
    // return extractExpiration(token).before(new Date());
    // }
}