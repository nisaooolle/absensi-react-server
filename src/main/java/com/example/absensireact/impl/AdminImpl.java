package com.example.absensireact.impl;


import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Admin;
import com.example.absensireact.model.LoginRequest;
import com.example.absensireact.repository.AdminRepository;
import com.example.absensireact.repository.UserRepository;
import com.example.absensireact.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    public AdminImpl(AdminRepository adminRepository, UserRepository userRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
    }


    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager authenticationManager;



    @Override
    public Admin RegisterAdmin(Admin admin) {
        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new NotFoundException("Email sudah digunakan");
        }
        admin.setUsername(admin.getUsername());
        admin.setPassword(encoder.encode(admin.getPassword()));
        admin.setRole("ADMIN");
        return adminRepository.save(admin);
    }

    @Override
    public Admin getById(Long id) {
        return adminRepository.findById(id).orElseThrow(() -> new NotFoundException("Id Admin Not Found"));
    }

    @Override
    public List<Admin> getAll() {
        return adminRepository.findAll();
    }

    @Override
    public Admin edit(Long id, Admin admin) {

        Admin existingUser = adminRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Admin tidak ditemukan"));

        existingUser.setUsername(admin.getUsername());
        existingUser.setEmail(admin.getEmail());
        return adminRepository.save(existingUser);
    }



    @Override
    public Map<String, Boolean> delete(Long id) {
        try {
            adminRepository.deleteById(id);
            Map<String, Boolean> res = new HashMap<>();
            res.put("Deleted", Boolean.TRUE);
            return res;
        } catch (Exception e) {
            return null;
        }
    }
}
