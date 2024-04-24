package com.example.absensireact.security;


import com.example.absensireact.detail.AdminDetail;
import com.example.absensireact.detail.UserDetail;
import com.example.absensireact.detail.UserDetailService;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import java.util.Date;
import java.util.Optional;
import java.util.function.Function;


@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private String jwtSecret = "login";
    private int jwtExpirationMs = 604800000;
    private static final String SECRET_KEY = "login";
    @Autowired
    UserDetailService userDetailService;

    public String generateToken(UserDetails userDetails) {
        // Mengambil ID dan peran dari UserDetails
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        Long id = null;
        if (userDetails instanceof UserDetail) {
            id = ((UserDetail) userDetails).getId();
        } else if (userDetails instanceof AdminDetail) {
            id = ((AdminDetail) userDetails).getId();
        }

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("id", id)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

}