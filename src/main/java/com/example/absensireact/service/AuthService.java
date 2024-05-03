package com.example.absensireact.service;



import com.example.absensireact.detail.CustomUserDetails;
import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Admin;
import com.example.absensireact.model.LoginRequest;
import com.example.absensireact.repository.AdminRepository;
import com.example.absensireact.repository.UserRepository;
import com.example.absensireact.securityNew.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthService  implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    CustomUserDetails customUserDetails;

    @Autowired
    AdminRepository adminRepository;



    public UserDetails loadUserByUsername(String email) {
        return customUserDetails.loadUserByUsername(email);
    }

    public Map<String, Object> loadUserByUsernameWithToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtTokenUtil.generateToken(authentication);

        Map<String, Object> response = new HashMap<>();
        response.put("data", userDetails);
        response.put("token", token);

        return response;
    }



}