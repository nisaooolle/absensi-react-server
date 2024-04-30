package com.example.absensireact.controller;

import com.example.absensireact.dto.ProfileAdminDTO;
import com.example.absensireact.dto.ProfilePhotoDTO;
import com.example.absensireact.impl.ProfileAdminImpl;
import com.example.absensireact.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProfileAdminController {

    @Autowired
    private ProfileAdminImpl profileAdminImpl;

    @PutMapping("/admin/profile/editDetail/{id}")
    public ResponseEntity<Admin> editProfile(@PathVariable Long id,
                                             @RequestBody ProfileAdminDTO adminDTO,
                                             @RequestParam(required = false) String oldPassword,
                                             @RequestParam(required = false) String newPassword,
                                             @RequestParam(required = false) String confirmPassword) {
        Admin admin = profileAdminImpl.editProfile(id, adminDTO, oldPassword, newPassword, confirmPassword);
        return ResponseEntity.ok(admin);
    }

    @PostMapping("/admin/profile/upload/{id}")
    public ResponseEntity<String> uploadProfilePhoto(
            @PathVariable Long id,
            @ModelAttribute ProfilePhotoDTO photoDTO) {
        try {
            MultipartFile picture = photoDTO.getPicture();

            if (picture == null || picture.isEmpty()) {
                throw new IllegalArgumentException("Please select a picture to upload");
            }

            String imageUrl = profileAdminImpl.uploadProfilePhoto(id, photoDTO);
            return ResponseEntity.ok(imageUrl);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload profile photo");
        }
    }
}

