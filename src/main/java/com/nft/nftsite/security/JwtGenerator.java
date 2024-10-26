package com.nft.nftsite.security;

import com.nft.nftsite.data.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtGenerator {

    @Value("${security.constants.jwt-exp-time}")
    private Long jwtExpTime;

    @Value("${security.constants.jwt-secret-key}")
    private String jwtSecretKey;

    public String generateToken(User user) {
        String username = user.getUsername();
        return generateToken(username, user.isActivated());
    }

    public String generateToken(Authentication authentication) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
        String username = authenticatedUser.getUsername();
        return generateToken(username, authenticatedUser.isAccountNonLocked());
    }

    public String generateToken(String username, boolean enabled) {
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + jwtExpTime);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey)
                .claim("enabled", enabled)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token).getBody();

        return claims.getSubject();
    }

    public boolean isUserActive(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token).getBody();

        return claims.get("enabled", Boolean.class);
    }

    public boolean validateToken(String token) {
        log.info("Validating JWT");
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecretKey)
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationCredentialsNotFoundException("Invalid token");
        }
    }
}
