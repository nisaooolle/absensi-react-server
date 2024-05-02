package com.example.absensireact.detail;

import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Admin;
import com.example.absensireact.model.SuperAdmin;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.AdminRepository;
import com.example.absensireact.repository.SuperAdminRepository;
import com.example.absensireact.repository.UserRepository;
import com.example.absensireact.securityNew.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class CustomUserDetails  implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    SuperAdminRepository superAdminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (adminRepository.existsByEmail(username)) {
            Admin admin = adminRepository.findByAdminEmail(username)
                    .orElseThrow(() -> new NotFoundException("Admin not found"));
            return AdminDetail.buildAdmin(admin);
        } else if (userRepository.existsByEmail(username)) {
            User user = userRepository.findByEmailUser(username)
                    .orElseThrow(() -> new NotFoundException("User not found"));
            return UserDetail.buidUser(user);
        } else if (superAdminRepository.findByEmailSuperAdmin(username).isPresent()) {
            SuperAdmin superAdmin = superAdminRepository.findByEmailSuperAdmin(username)
                    .orElseThrow(() -> new NotFoundException("Super Admin not found"));
            return SuperAdminDetail.buildSuperAdmin(superAdmin);
        }
        throw new NotFoundException("User Not Found with username: " + username);
    }

    public UserDetails loadUserDetailsForAttendance(String username) {
        if (adminRepository.existsByEmail(username)) {
            Admin admin = adminRepository.findByAdminEmail(username)
                    .orElseThrow(() -> new NotFoundException("Admin not found"));
            return AdminDetail.buildAdmin(admin);
        } else if (userRepository.existsByEmail(username)) {
            User user = userRepository.findByEmailUser(username)
                    .orElseThrow(() -> new NotFoundException("User not found"));
            return UserDetail.buidUser(user);
        } else if (superAdminRepository.findByEmailSuperAdmin(username).isPresent()) {
            SuperAdmin superAdmin = superAdminRepository.findByEmailSuperAdmin(username)
                    .orElseThrow(() -> new NotFoundException("Super Admin not found"));
            return SuperAdminDetail.buildSuperAdmin(superAdmin);
        }
        throw new NotFoundException("User Not Found with username: " + username);
    }

}
