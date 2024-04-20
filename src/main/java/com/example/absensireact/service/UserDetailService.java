package com.example.absensireact.service;

import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        if (userRepository.existsByEmail(username)){
            User user = userRepository.findByEmail(username).orElseThrow(() -> new NotFoundException("Id Not Found"));
            return UserDetail.buildUser(user);
        }
        throw new NotFoundException("Entity not found");
    }

}