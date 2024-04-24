package com.example.absensireact.detail;

import com.example.absensireact.detail.UserDetail;
import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Admin;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.AdminRepository;
import com.example.absensireact.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AdminRepository adminRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        Object userOrAdmin = getUserOrAdminByEmail(username);
        if (userOrAdmin instanceof User) {
            return UserDetail.buildUser((User) userOrAdmin);
        } else if (userOrAdmin instanceof Admin) {
            return AdminDetail.buildAdmin((Admin) userOrAdmin);
        } else {
            throw new NotFoundException("Entity not found");
        }
    }

    public Optional<Object> getUserOrAdminByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            return Optional.of(userOptional.get());
        } else {
            Optional<Admin> adminOptional = adminRepository.findByEmail(email);
            return Optional.ofNullable(adminOptional);
        }
    }
}