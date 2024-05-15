package com.example.absensireact.impl;

import com.example.absensireact.config.AppConfig;
import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.*;
import com.example.absensireact.repository.*;
import com.example.absensireact.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class UserImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JabatanRepository jabatanRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private OrganisasiRepository organisasiRepository;

    @Autowired
    private AppConfig appConfig;



    @Autowired
    PasswordEncoder encoder;


    @Autowired
    AuthenticationManager authenticationManager;


    @Override
    public User Register(User user, Organisasi organisasi) {
        if (adminRepository.existsByEmail(user.getEmail())) {
            throw new NotFoundException("Email sudah digunakan oleh admin");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new NotFoundException("Email sudah digunakan oleh user ");
        }

        user.setUsername(user.getUsername());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setOrganisasi(organisasi);
        user.setRole("USER");
        return userRepository.save(user);
    }


    @Override
    public List<User> GetAllKaryawanByIdAdmin(Long idAdmin){
        return userRepository.findByIdAdmin(idAdmin);
    }

    @Override
    public User Tambahkaryawan(Long idAdmin, User user){
        Optional<Admin> adminvalidate = adminRepository.findById(idAdmin);

        if (adminvalidate.isPresent()) {
            user.setRole("USER");
            user.setPassword(encoder.encode(user.getPassword()));
            user.setEmail(user.getEmail());
            user.setUsername(user.getUsername());

            Organisasi organisasi = organisasiRepository.findById(user.getOrganisasi().getId())
                    .orElseThrow(() -> new NotFoundException("Organisasi tidak ditemukan"));
            Jabatan jabatan = jabatanRepository.findById(user.getJabatan().getIdJabatan())
                    .orElseThrow(() -> new NotFoundException("Jabatan tidak ditemukan"));
            Shift shift = shiftRepository.findById(user.getShift().getId())
                    .orElseThrow(() -> new NotFoundException("Shift tidak ditemukan"));

            user.setOrganisasi(organisasi);
            user.setJabatan(jabatan);
            user.setShift(shift);

            return userRepository.save(user);
        }

        throw new NotFoundException("Id Admin tidak ditemukan");
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Id Not Found"));
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User edit(Long id, User user) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User tidak ditemukan"));

        existingUser.setUsername(user.getUsername());
        existingUser.setOrganisasi(user.getOrganisasi());
        existingUser.setEmail(user.getEmail());
        return userRepository.save(existingUser);
    }



    @Override
    public Map<String, Boolean> delete(Long id) {
        try {
            userRepository.deleteById(id);
            Map<String, Boolean> res = new HashMap<>();
            res.put("Deleted", Boolean.TRUE);
            return res;
        } catch (Exception e) {
            return null;
        }
    }

}
