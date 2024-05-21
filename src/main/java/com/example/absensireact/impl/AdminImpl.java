package com.example.absensireact.impl;


import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Admin;
import com.example.absensireact.model.SuperAdmin;
import com.example.absensireact.repository.AdminRepository;
import com.example.absensireact.repository.SuperAdminRepository;
import com.example.absensireact.repository.UserRepository;
import com.example.absensireact.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    private final SuperAdminRepository superAdminRepository;

    public AdminImpl(AdminRepository adminRepository, UserRepository userRepository, SuperAdminRepository superAdminRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.superAdminRepository = superAdminRepository;
    }


    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager authenticationManager;



    @Override
    public Admin RegisterBySuperAdmin(Long idSuperAdmin , Admin admin){
        Optional<SuperAdmin> superAdminOptional = superAdminRepository.findById(idSuperAdmin);
        if (superAdminOptional.isPresent()) {
            if (adminRepository.existsByEmail(admin.getEmail())) {
                throw new NotFoundException("Email sudah digunakan");
            }
            SuperAdmin superAdmin = superAdminOptional.get();

            admin.setSuperAdmin(superAdmin);
            admin.setUsername(admin.getUsername());
            admin.setPassword(encoder.encode(admin.getPassword()));
            admin.setRole("ADMIN");
            return adminRepository.save(admin);
        }
        throw new NotFoundException("Id super admin tidak ditemukan");
    }

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
