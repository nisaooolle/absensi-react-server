package com.example.absensireact.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FirebaseService {

    public String uploadProfilePhoto(Long id, MultipartFile file) {
        // Implementasi pengunggahan foto profil ke Firebase Storage
        // Pastikan untuk memberikan nama file yang unik untuk setiap pengguna
        // dan kembalikan URL gambar setelah berhasil diunggah
        // Contoh: firebaseStorage.upload("/profile_photos/" + id + "/" + file.getOriginalFilename(), file.getInputStream());
        return "https://example.com/profile_photos/" + id + "/" + file.getOriginalFilename(); // URL gambar hasil pengunggahan
    }
}

