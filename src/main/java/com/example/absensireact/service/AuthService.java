package com.example.absensireact.service;



import com.example.absensireact.model.Admin;
import com.example.absensireact.repository.AdminRepository;
import com.example.absensireact.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AuthService  implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AdminRepository adminRepository;

    // mencari user berdasarkan username
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Admin adminModel = adminRepository.findByEmail(email);
        if (adminModel != null) {
            // Cek jika pengguna memiliki peran admin
            if (adminModel.getRole().equals("ADMIN")) {
                List<SimpleGrantedAuthority> roles = Arrays.asList(new SimpleGrantedAuthority("ADMIN"));
                return new User(adminModel.getEmail(), adminModel.getPassword() ,roles);
            }
        }

        com.example.absensireact.model.User userModel = userRepository.findByEmail(email);
        if (userModel != null) {
            // Cek jika pengguna memiliki peran admin
            if (userModel.getRole().equals("USER")) {
                List<SimpleGrantedAuthority> roles = Arrays.asList(new SimpleGrantedAuthority("USER"));
                return new User(userModel.getEmail(), userModel.getPassword(), roles);
            }
        }


        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}