package com.example.absensireact.impl;

import com.example.absensireact.dto.ProfileAdminDTO;
import com.example.absensireact.dto.ProfilePhotoDTO;
import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Admin;
import com.example.absensireact.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class ProfileAdminImpl {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder encoder;

    public Admin editProfile(Long id, ProfileAdminDTO adminDTO, String oldPassword, String newPassword, String confirmPassword) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Admin not found"));

        // Update username and email
        admin.setUsername(adminDTO.getUsername());
        admin.setEmail(adminDTO.getEmail());

        // Update password if provided
        if (newPassword != null && !newPassword.isEmpty()) {
            if (encoder.matches(oldPassword, admin.getPassword()) &&
                    newPassword.equals(confirmPassword)) {
                admin.setPassword(encoder.encode(newPassword));
            } else {
                throw new IllegalArgumentException("Invalid old password or new password mismatch.");
            }
        }

        return adminRepository.save(admin);
    }

    public String uploadProfilePhoto(Long id, ProfilePhotoDTO photoDTO) throws IOException {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Admin not found"));

        MultipartFile picture = photoDTO.getPicture();

        if (picture == null || picture.isEmpty()) {
            throw new IllegalArgumentException("Please select a picture to upload");
        }

        String fileName = id + "_" + picture.getOriginalFilename();
        String uploadDir = "uploads/";

        // Save the file to the filesystem
        File file = new File(uploadDir + fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(picture.getBytes());
        }

        // Update profile photo URL in database
        String imageUrl = uploadDir + fileName;
        admin.setImageAdmin(imageUrl);
        adminRepository.save(admin);

        return imageUrl;
    }


}
