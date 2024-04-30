//package com.example.absensireact.controller;
//
//import com.example.absensireact.dto.ProfileDTO;
//import com.example.absensireact.impl.ProfileImpl;
//import com.example.absensireact.model.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api")
//public class ProfileController {
//
//    @Autowired
//    private ProfileImpl profileImpl;
//
//    @PutMapping("/profile/edit/{id}")
//    public ResponseEntity<User> editProfile(@PathVariable Long id,
//                                            @ModelAttribute ProfileDTO profileDTO,
//                                            @RequestParam String oldPassword,
//                                            @RequestParam String newPassword,
//                                            @RequestParam String confirmPassword) {
//        User user = profileImpl.editProfile(id, profileDTO, oldPassword, newPassword, confirmPassword);
//        return ResponseEntity.ok(user);
//    }
//}
