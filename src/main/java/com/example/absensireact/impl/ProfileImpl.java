package com.example.absensireact.impl;

import com.example.absensireact.exception.ProfileNotFoundException;
import com.example.absensireact.model.Profile;
import com.example.absensireact.repository.ProfileRepository;
import com.example.absensireact.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Optional;

@Service
public class ProfileImpl implements ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public void saveProfile(String name, MultipartFile picture) {
        try {
            Profile profile = new Profile(null, name, picture.getBytes());
            profileRepository.save(profile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void updateProfile(Long id, String name, MultipartFile picture) {
        Optional<Profile> optionalProfile = profileRepository.findById(id);
        if (optionalProfile.isPresent()) {
            Profile profile = optionalProfile.get();

            // Update profile name
            profile.setName(name);

            // Check if new picture is provided
            if (picture != null && !picture.isEmpty()) {
                try {
                    // Update profile picture
                    profile.setPicture(picture.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Failed to update profile picture");
                }
            }

            // Save the updated profile
            profileRepository.save(profile);
        } else {
            throw new ProfileNotFoundException("Profile with ID " + id + " not found");
        }
    }



    @Override
    public void updateProfileName(Long id, String name) {

    }

    @Override
    public void updateProfilePicture(Long id, MultipartFile picture) {

    }


    @Override
    public void deleteProfile(Long id) {
        profileRepository.deleteById(id);
    }

}

