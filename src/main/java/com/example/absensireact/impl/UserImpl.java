package com.example.absensireact.impl;

import com.example.absensireact.model.LoginRequest;
import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.UserRepository;
import com.example.absensireact.security.JwtUtils;
import com.example.absensireact.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager authenticationManager;


    @Override
    public Map<Object, Object> login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("Email not found"));

        if (encoder.matches(loginRequest.getPassword(), user.getPassword())) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            return redirectAndValidate(authentication, user);
        }

        throw new NotFoundException("Invalid password");
    }

    private Map<Object, Object> redirectAndValidate(Authentication authentication, User user) {
        String jwt = jwtUtils.generateToken(authentication);
        userRepository.save(user);
        Map<Object, Object> response = new HashMap<>();
        response.put("data", user);
        response.put("id", user.getId());
        response.put("token", jwt);

        return response;
    }

    @Override
    public User Register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new NotFoundException("Email sudah digunakan");
        }
        user.setUsername(user.getUsername());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setOrganisasi(user.getOrganisasi());
        return userRepository.save(user);
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
