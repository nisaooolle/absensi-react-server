package com.example.absensireact.impl;

import com.example.absensireact.dto.UserDTO;
import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.UserRepository;
import com.example.absensireact.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProfileImpl {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private FirebaseService firebaseService;

    public User editProfile(Long id, UserDTO userDTO, String oldPassword, String newPassword, String confirmPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Update username and email
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());

        // Update password if provided
        if (newPassword != null && !newPassword.isEmpty()) {
            if (encoder.matches(oldPassword, user.getPassword()) &&
                    newPassword.equals(confirmPassword)) {
                user.setPassword(encoder.encode(newPassword));
            } else {
                throw new IllegalArgumentException("Invalid old password or new password mismatch.");
            }
        }

        return userRepository.save(user);
    }

    public String uploadProfilePhoto(Long id, MultipartFile file) {
        // Panggil method di FirebaseService untuk mengunggah foto profil ke Firebase Storage
        return firebaseService.uploadProfilePhoto(id, file);
    }
}
