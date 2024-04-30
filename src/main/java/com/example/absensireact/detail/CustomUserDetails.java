package com.example.absensireact.detail;

import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Admin;
import com.example.absensireact.model.SuperAdmin;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.AdminRepository;
import com.example.absensireact.repository.SuperAdminRepository;
import com.example.absensireact.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    SuperAdminRepository superAdminRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        Optional<SuperAdmin> superAdmin = superAdminRepository.findByEmailSuperAdmin(username);
        if (superAdmin.isPresent()) {
            return SuperAdminDetail.buildSuperAdmin(superAdmin.get());
        }

        Optional<Admin> admin = adminRepository.findByAdminEmail(username);
        if (admin.isPresent()) {
            return AdminDetail.buildAdmin(admin.get());
        }

        Optional<User> user = userRepository.findByEmailUser(username);
        if (user.isPresent()) {
            return UserDetail.buidUser(user.get());
        }

        throw new UsernameNotFoundException("User Not Found with username: " + username);
    }


}
