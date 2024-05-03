package com.example.absensireact.securityNew;

import com.example.absensireact.detail.AdminDetail;
import com.example.absensireact.detail.SuperAdminDetail;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.UserRepository;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil implements Serializable {
//    @Autowired
//    private UserRepository userRepository;
//
//    public String generateToken(UserDetails userDetails) {
//        String token = UUID.randomUUID().toString().replace("-", "");
//        User akuns = userRepository.findByEmail(userDetails.getUsername());
//        var checkingToken = tokenRepo.findByAkunId(akuns.getId());
//        if (checkingToken.isPresent()) tokenRepo.deleteById(checkingToken.get().getId());
//        TemporaryToken temporaryToken = new TemporaryToken();
//        temporaryToken.setToken(token);
//        temporaryToken.setExpiredDate(new Date(new Date().getTime() + expired));
//        temporaryToken.setAkunId(akuns.getId());
//        tokenRepo.save(temporaryToken);
//        return token;
//    }
//
//    //    get token
//    public TemporaryToken getSubject(String token) {
//        return tokenRepo.findByToken(token).orElseThrow(() -> new InternalErrorException("Token error!"));
//    }
//
//    //    mengecek token
//    public boolean checkingTokenJwt(String token) {
//        TemporaryToken tokenExist = tokenRepo.findByToken(token).orElse(null);
//        if (tokenExist == null) {
//            System.out.println("Token kosong");
//            return false;
//        }
//        if (tokenExist.getExpiredDate().before(new Date())) {
//            System.out.println("Token expired");
//            return false;
//        }
//        return true;
//    }
private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long jwtExpirationMs;

    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        if (authentication.getPrincipal() instanceof AdminDetail adminDetails) {
            claims.put("admin_id", adminDetails.getId());
        } else if (authentication.getPrincipal() instanceof SuperAdminDetail) {
            claims.put("roles", "SUPERADMIN");
        }

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public boolean validateToken(String token) {

        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
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


    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public List<GrantedAuthority> getRolesFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        List<String> roleNames = claims.get("roles", List.class);
        return roleNames.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}