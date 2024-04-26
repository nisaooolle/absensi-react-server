package com.example.absensireact.detail;

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

@Service
public class CustomUserDetails  implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AdminRepository adminRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        if (adminRepository.findByAdminEmail(username).isPresent()) {
            Admin admin = adminRepository.findByAdminEmail(username).orElseThrow(() -> new NotFoundException("Username not found"));;
            return AdminDetail.buildAdmin(admin);
        } else if (userRepository.existsByEmail(username)){
            User user = userRepository.findByEmailUser(username).orElseThrow(() -> new NotFoundException("Username not found"));;
            return UserDetail.buidUser(user);
        }
        throw new NotFoundException("User Not Found with username: " + username);
    }


}
