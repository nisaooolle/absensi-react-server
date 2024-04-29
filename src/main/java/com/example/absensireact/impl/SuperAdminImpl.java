package com.example.absensireact.impl;

import com.example.absensireact.exception.InternalErrorException;
import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.SuperAdmin;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.SuperAdminRepository;
import com.example.absensireact.service.SuperAdminService;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class SuperAdminImpl implements SuperAdminService {

    static final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/absensireact.appspot.com/o/%s?alt=media";

    private final SuperAdminRepository superAdminRepository;

    @Autowired
    PasswordEncoder encoder;


    public SuperAdminImpl(SuperAdminRepository superAdminRepository) {
        this.superAdminRepository = superAdminRepository;
    }

    @Override
    public SuperAdmin getAllSuperAdmin(){
        return (SuperAdmin) superAdminRepository.findAll();
    }

    @Override
    public Optional<SuperAdmin> getSuperadminbyId(Long id){
       return superAdminRepository.findById(id);
    }

    @Override
    public SuperAdmin RegisterSuperAdmin(SuperAdmin superAdmin) {
        if (superAdminRepository.existsByEmail(superAdmin.getEmail())) {
            throw new NotFoundException("Email sudah digunakan  ");
        }
        superAdmin.setUsername(superAdmin.getUsername());
        superAdmin.setPassword(encoder.encode(superAdmin.getPassword()));
        superAdmin.setRole("SUPERADMIN");
        return superAdminRepository.save(superAdmin);
    }

    @Override
    public SuperAdmin tambahSuperAdmin(Long id, SuperAdmin superAdmin, MultipartFile image){
       Optional<SuperAdmin> ExistingSuperAdmin = Optional.ofNullable(superAdminRepository.findById(id).orElse(null));
        if (ExistingSuperAdmin == null) {
            superAdmin.setEmail(superAdmin.getEmail());
            superAdmin.setUsername(superAdmin.getUsername());
            superAdmin.setImageAdmin(imageConverter(image));

            return superAdminRepository.save(superAdmin);

        }

        throw new NotFoundException("Id Super Admin tidak ditemukan");
    }

    @Override
    public SuperAdmin EditSuperAdmin(Long id, MultipartFile image, SuperAdmin superAdmin){
        Optional<SuperAdmin> ifSuperAdmin = Optional.ofNullable(superAdminRepository.findById(id).orElse(null));
        if (ifSuperAdmin == null) {
            superAdmin.setEmail(superAdmin.getEmail());
            superAdmin.setUsername(superAdmin.getUsername());
            superAdmin.setImageAdmin(imageConverter(image));

            return superAdminRepository.save(superAdmin);
        }
       throw new NotFoundException("Id Super Admin tidak ditemukan");
    }

    @Override
    public void deleteSuperAdmin(Long id) {
        if (superAdminRepository.existsById(id)) {
            superAdminRepository.deleteById(id);
        } else {
            throw new NotFoundException("Super Admin not found with id: " + id);
        }
    }



    private String imageConverter(MultipartFile multipartFile) {
        try {
            return uploadFoto(multipartFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalErrorException("Error upload file");
        }
    }

    public String uploadFoto(MultipartFile image) throws IOException {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String folderPath = "superAdmin/";
        String fileName = folderPath + timestamp + "_" + image.getOriginalFilename();
        BlobId blobId = BlobId.of("absensireact.appspot.com", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(image.getContentType()) // Set content type from image
                .build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./src/main/java/com.example.absensireact/firebase/FirebaseConfig.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, image.getBytes()); // Use image.getBytes() to get file bytes directly
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File file = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return file;
    }
    private String getExtentions(String fileName) {
        return fileName.split("\\.")[0];
    }

}
