//package com.example.absensireact.impl;
//
//import com.example.absensireact.dto.ProfileDTO;
//import com.example.absensireact.exception.NotFoundException;
//import com.example.absensireact.model.User;
//import com.example.absensireact.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class ProfileImpl {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder encoder;
//
//    public User editProfile(Long id, ProfileDTO profileDTO, String oldPassword, String newPassword, String confirmPassword) {
//        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
//
//        // Update profile details
//        user.setUsername(profileDTO.getUsername());
//        user.setEmail(profileDTO.getEmail());
//        user.setOrganisasiId(profileDTO.getOrganisasiId());
//        user.setJabatanId(profileDTO.getJabatanId());
//
//        // Password update logic
//        if (newPassword != null && !newPassword.isEmpty()) {
//            if (encoder.matches(oldPassword, user.getPassword()) && newPassword.equals(confirmPassword)) {
//                user.setPassword(encoder.encode(newPassword));
//            } else {
//                throw new IllegalArgumentException("Invalid old password or new password mismatch.");
//            }
//        }
//
//        return userRepository.save(user);
//    }
//}
