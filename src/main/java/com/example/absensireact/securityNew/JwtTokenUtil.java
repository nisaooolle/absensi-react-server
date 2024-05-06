package com.example.absensireact.securityNew;


import com.example.absensireact.detail.AdminDetail;
import com.example.absensireact.detail.SuperAdminDetail;
import com.example.absensireact.detail.UserDetail;
import com.example.absensireact.model.Admin;
import com.example.absensireact.model.SuperAdmin;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.AdminRepository;
import com.example.absensireact.repository.SuperAdminRepository;
import com.example.absensireact.repository.UserRepository;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.Security;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

    private String jwtSecret = "absensi";
    private int jwtExpirationMs = 604800000;

    private static final String SECRET_KEY = "absensi";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private SuperAdminRepository superAdminRepository;


//    public String generateJwtToken(Authentication authentication) {
//        AdminDetail adminPrincipal = (AdminDetail) authentication.getPrincipal();
//        Admin admin = adminRepository.findByEmail(adminPrincipal.getUsername()).get();
//        return Jwts.builder()
//                .claim("id" , adminPrincipal.getId())
//                .setAudience("ADMIN")
//                .claim("data", admin)
//                .setSubject((adminPrincipal.getUsername()))
//                .setIssuedAt(new Date())
//                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
//                .signWith(SignatureAlgorithm.HS512, jwtSecret)
//                .compact();
//    }
//    public String generateToken(Authentication authentication) {
//        UserDetail userPrincipal = (UserDetail) authentication.getPrincipal();
//        User user = userRepository.findByEmail(userPrincipal.getUsername()).get();
//        return Jwts.builder()
//                .claim("data", user)
//                .setSubject(userPrincipal.getUsername())
//                .claim("id" , userPrincipal.getId())
//                .setAudience("USER")
//                .setIssuedAt(new Date())
//                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                .compact();
//    }
//
//
//
//
//    public String generateTokenSuperAdmin(Authentication authentication) {
//        SuperAdminDetail adminPrincipal = (SuperAdminDetail) authentication.getPrincipal();
//        SuperAdmin superAdmin = superAdminRepository.findByEmail(adminPrincipal.getUsername()).get();
//        return Jwts.builder()
//                .setSubject(adminPrincipal.getUsername())
//                .claim("id" , adminPrincipal.getId())
//                .setAudience("SUPERADMIN")
//                .claim("data", superAdmin)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                .compact();
//    }

//        public String generateJwtToken(Admin admin) {;
//            return Jwts.builder()
//                .claim("id" , admin.getId())
//                .setAudience("ADMIN")
//                .claim("data", admin)
//                .setSubject((admin.getUsername()))
//                .setIssuedAt(new Date())
//                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
//                .signWith(SignatureAlgorithm.HS256, jwtSecret)
//                .compact();
//    }
//
//    public String generateToken(User user) {
//        return Jwts.builder()
//                .claim("data", user)
//                .setSubject(user.getUsername())
//                .claim("id" , user.getId())
//                .claim("role" , "USER")
//                .setIssuedAt(new Date())
//                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                .compact();
//    }
//
//    public String generetaTokenSuperadmin(SuperAdmin superAdmin) {
//        return Jwts.builder()
//                .setSubject(superAdmin.getUsername())
//                .claim("id", superAdmin.getId())
//                .claim("role", "SUPERADMIN")
//                .claim("data" , superAdmin)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
//                .signWith(SignatureAlgorithm.HS256, jwtSecret)
//                .compact();
//    }
    public static Claims decodeJwt(String jwtToken) {
        Jws<Claims> jwsClaims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(jwtToken);

        return jwsClaims.getBody();
    }


    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("id", getUserIdFromUserDetails(userDetails))
                .claim("role", getRoleFromUserDetails(userDetails))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    private Long getUserIdFromUserDetails(UserDetails userDetails) {
        if (userDetails instanceof UserDetail) {
            return ((UserDetail) userDetails).getId();
        } else if (userDetails instanceof AdminDetail) {
            return ((AdminDetail) userDetails).getId();
        } else if (userDetails instanceof SuperAdminDetail) {
            return ((SuperAdminDetail) userDetails).getId();
        }
        return null;
    }

    private String getRoleFromUserDetails(UserDetails userDetails) {
        if (userDetails instanceof UserDetail) {
            return "USER";
        } else if (userDetails instanceof AdminDetail) {
            return "ADMIN";
        } else if (userDetails instanceof SuperAdminDetail) {
            return "SUPERADMIN";
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }
}