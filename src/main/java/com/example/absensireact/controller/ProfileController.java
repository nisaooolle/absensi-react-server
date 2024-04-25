package com.example.absensireact.controller;

import com.example.absensireact.model.ProfileRequest;
import com.example.absensireact.service.ProfileService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    @ApiOperation(value = "Upload a new profile picture", notes = "Upload a new profile picture along with the name")
    public ResponseEntity<String> uploadProfile(
            @ApiParam(value = "Profile details including the picture", required = true)
            @ModelAttribute ProfileRequest profileRequest) {
        MultipartFile picture = profileRequest.getPicture();
        if (picture == null || picture.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a picture to upload");
        }
        profileService.saveProfile(profileRequest.getName(), picture);
        return ResponseEntity.ok().body("Successfully uploaded profile picture for: " + profileRequest.getName());
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a profile", notes = "Delete a profile by its ID")
    public ResponseEntity<String> deleteProfile(@PathVariable Long id) {
        profileService.deleteProfile(id);
        return ResponseEntity.ok().body("Profile deleted successfully");
    }

        @PutMapping("/{id}")
        @ApiOperation(value = "Update a profile", notes = "Update a profile's name and/or picture by its ID")
        public ResponseEntity<String> updateProfile(@PathVariable Long id, @ModelAttribute ProfileRequest profileRequest) {
            try {
                MultipartFile picture = profileRequest.getPicture();

                // Check if name and/or picture are provided
                if (profileRequest.getName() != null || (picture != null && !picture.isEmpty())) {
                    if (profileRequest.getName() != null && (picture != null && !picture.isEmpty())) {
                        // Update profile with both name and picture
                        profileService.updateProfile(id, profileRequest.getName(), picture);
                        return ResponseEntity.ok().body("Profile updated successfully with name and picture");
                    } else if (profileRequest.getName() != null) {
                        // Update profile with name only
                        profileService.updateProfileName(id, profileRequest.getName());
                        return ResponseEntity.ok().body("Profile name updated successfully");
                    } else {
                        // Update profile with picture only
                        profileService.updateProfilePicture(id, picture);
                        return ResponseEntity.ok().body("Profile picture updated successfully");
                    }
                } else {
                    return ResponseEntity.badRequest().body("Please provide at least a name or a picture for updating the profile");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update profile: " + e.getMessage());
            }
        }
    }





