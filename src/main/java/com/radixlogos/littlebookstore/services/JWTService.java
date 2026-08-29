package com.radixlogos.littlebookstore.services;


import com.radixlogos.littlebookstore.config.JWTProperties;
import com.radixlogos.littlebookstore.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTService {

    private final JWTProperties jwtProperties;

    public JWTService(JWTProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String generateToken(User user){
        return  Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                .claim("role",user.getAuthorities().iterator().next().getAuthority())
                .signWith(getSingingKey())
                .compact();
    }

    public String extractUsername(String token){

        return extractClaim(token,Claims::getSubject).toString();
    }

    public boolean isTokenValid(String token, User user){
        return extractUsername(token).equals(user.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expirationDate = extractExpiration(token);
        return expirationDate.before(new Date());
    }

    public Date extractExpiration(String token){
        return (Date) extractClaim(token,Claims::getExpiration);
    }


    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSingingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private <T> Object extractClaim(String token, Function<Claims,T> resolver){
        Claims claims = extractAllClaims(token);

        return resolver.apply(claims);
    }
    public SecretKey getSingingKey(){
        return Keys.hmacShaKeyFor(
                jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)
        );
    }
}
