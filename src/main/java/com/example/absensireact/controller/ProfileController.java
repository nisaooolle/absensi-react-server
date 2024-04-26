package com.example.absensireact.controller;

import com.example.absensireact.dto.UserDTO;
import com.example.absensireact.impl.ProfileImpl;
import com.example.absensireact.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ProfileController {

    @Autowired
    private ProfileImpl profileImpl;

    @PutMapping("/profile/edit/{id}")
    public ResponseEntity<User> editProfile(@PathVariable Long id,
                                            @ModelAttribute UserDTO userDTO,
                                            @RequestParam String oldPassword,
                                            @RequestParam String newPassword,
                                            @RequestParam String confirmPassword) {
        User user = profileImpl.editProfile(id, userDTO, oldPassword, newPassword, confirmPassword);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/profile/upload/{id}")
    public ResponseEntity<String> uploadProfilePhoto(@PathVariable Long id,
                                                     @RequestParam("file") MultipartFile file) {
        String imageUrl = profileImpl.uploadProfilePhoto(id, file);
        return ResponseEntity.ok(imageUrl);
    }
}
