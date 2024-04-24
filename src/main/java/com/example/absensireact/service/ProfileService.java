package com.example.absensireact.service;

import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {

    void saveProfile(String name, MultipartFile picture);

    void deleteProfile(Long id);

    void updateProfile(Long id, String name, MultipartFile picture);

    void updateProfileName(Long id, String name);

    void updateProfilePicture(Long id, MultipartFile picture);
}


