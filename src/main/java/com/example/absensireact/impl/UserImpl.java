package com.example.absensireact.impl;

import com.example.absensireact.detail.AdminDetail;
import com.example.absensireact.detail.UserDetail;
import com.example.absensireact.detail.UserDetailService;
import com.example.absensireact.model.Admin;
import com.example.absensireact.model.LoginRequest;
import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.AdminRepository;
import com.example.absensireact.repository.UserRepository;
import com.example.absensireact.security.JwtUtils;
import com.example.absensireact.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
    private JwtUtils jwtUtils;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDetailService userDetailService;


    @Override
    public Map<Object, Object> login(LoginRequest loginRequest) {
        Object userOrAdmin = getUserOrAdminByEmail(loginRequest.getEmail());

        if (userOrAdmin == null) {
            throw new BadCredentialsException("Invalid email or password");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        if (userOrAdmin instanceof User) {
            User user = (User) userOrAdmin;
            if (!user.getPassword().equals(loginRequest.getPassword())) {
                throw new BadCredentialsException("Invalid password");
            }
            return redirectAndValidate(authentication, user);
        } else if (userOrAdmin instanceof Admin) {
            Admin admin = (Admin) userOrAdmin;
            if (!admin.getPassword().equals(loginRequest.getPassword())) {
                throw new BadCredentialsException("Invalid password");
            }
            return redirectAndValidate(authentication, admin);
        }

        throw new BadCredentialsException("Invalid email or password");
    }

    private Map<Object, Object> redirectAndValidate(Authentication authentication, Object userOrAdmin) {
        String jwt = jwtUtils.generateToken((UserDetails) authentication);
        if (userOrAdmin instanceof User) {
            User user = (User) userOrAdmin;
            userRepository.save(user);
            Map<Object, Object> response = new HashMap<>();
            response.put("data", user);
            response.put("id", user.getId());
            response.put("token", jwt);
            return response;
        } else if (userOrAdmin instanceof Admin) {
            Admin admin = (Admin) userOrAdmin;
            adminRepository.save(admin);
            Map<Object, Object> response = new HashMap<>();
            response.put("data", admin);
            response.put("id", admin.getId());
            response.put("token", jwt);
            return response;
        }

        throw new BadCredentialsException("Invalid email or password");
    }

    public Optional<Object> getUserOrAdminByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            return Optional.of(userOptional.get());
        } else {
            Optional<Admin> adminOptional = adminRepository.findByEmail(email);
            if (adminOptional.isPresent()) {
                return Optional.of(adminOptional.get());
            } else {
                return Optional.empty();
            }
        }
    }


    @Override
    public User Register(User user) {
        if (adminRepository.existsByEmail(user.getEmail())) {
            throw new NotFoundException("Email sudah digunakan oleh admin");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new NotFoundException("Email sudah digunakan oleh user ");
        }

        user.setUsername(user.getUsername());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setOrganisasi(user.getOrganisasi());
        user.setRole("USER");
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
