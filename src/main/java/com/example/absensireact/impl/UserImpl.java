package com.example.absensireact.impl;

import com.example.absensireact.config.AppConfig;
import com.example.absensireact.exception.BadRequestException;
import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.*;
import com.example.absensireact.repository.*;
import com.example.absensireact.service.UserService;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;


@Service
public class UserImpl implements UserService {

    static final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/absensireact.appspot.com/o/%s?alt=media";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JabatanRepository jabatanRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private OrganisasiRepository organisasiRepository;

    @Autowired
    private AppConfig appConfig;



    @Autowired
    PasswordEncoder encoder;


    @Autowired
    AuthenticationManager authenticationManager;


    public User Register(User user, Long idOrganisasi) {
        if (adminRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email sudah digunakan oleh admin");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email sudah digunakan oleh user");
        }

        Organisasi organisasi = organisasiRepository.findById(idOrganisasi)
                .orElseThrow(() -> new NotFoundException("Organisasi tidak ditemukan"));

        Admin admin = organisasi.getAdmin();
        if (admin == null) {
            throw new NotFoundException("Admin tidak ditemukan untuk organisasi tersebut");
        }

        user.setJabatan(null);
        user.setShift(null);
        user.setAdmin(admin);
        user.setUsername(user.getUsername());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setOrganisasi(organisasi);
        user.setRole("USER");

        return userRepository.save(user);
    }

    @Override
    public List<User> GetAllKaryawanByIdAdmin(Long idAdmin){
        return userRepository.findByIdAdmin(idAdmin);
    }

    @Override
    public User Tambahkaryawan(Long idAdmin, User user){
        Optional<Admin> adminvalidate = adminRepository.findById(idAdmin);

        if (adminvalidate.isPresent()) {
            user.setRole("USER");
            user.setPassword(encoder.encode(user.getPassword()));
            user.setEmail(user.getEmail());
            user.setUsername(user.getUsername());

            Organisasi organisasi = organisasiRepository.findById(user.getOrganisasi().getId())
                    .orElseThrow(() -> new NotFoundException("Organisasi tidak ditemukan"));
            Jabatan jabatan = jabatanRepository.findById(user.getJabatan().getIdJabatan())
                    .orElseThrow(() -> new NotFoundException("Jabatan tidak ditemukan"));
            Shift shift = shiftRepository.findById(user.getShift().getId())
                    .orElseThrow(() -> new NotFoundException("Shift tidak ditemukan"));

            user.setOrganisasi(organisasi);
            user.setJabatan(jabatan);
            user.setShift(shift);

            return userRepository.save(user);
        }

        throw new NotFoundException("Id Admin tidak ditemukan");
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Id Not Found"));
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public  User fotoUser(Long id, MultipartFile image) throws  IOException{
        User exisUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User tidak ditemukan"));
        String file = uploadFoto(image , "user");
        exisUser.setFotoUser(file);
        return userRepository.save(exisUser);
    }
    @Override
    public User edit(Long id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User tidak ditemukan"));

        existingUser.setUsername(user.getUsername());
        existingUser.setOrganisasi(user.getOrganisasi());
        existingUser.setEmail(user.getEmail());
        return userRepository.save(existingUser);


    }

        private String uploadFoto(MultipartFile multipartFile, String fileName) throws IOException {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String folderPath = "user/";
        String fullPath = folderPath + timestamp + "_" + fileName;
        BlobId blobId = BlobId.of("absensireact.appspot.com", fullPath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./src/main/resources/FirebaseConfig.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, multipartFile.getBytes());
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fullPath, StandardCharsets.UTF_8));
    }


    private void deleteFoto(String fileName) throws IOException {
        BlobId blobId = BlobId.of("absensireact.appspot.com", fileName);
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./src/main/resources/FirebaseConfig.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.delete(blobId);
    }



    @Override
   public void delete(Long id) throws IOException {
    Optional<User> userOptional = userRepository.findById(id);
    if (userOptional.isPresent()) {
        User user = userOptional.get();
        String fotoUrl = user.getFotoUser();
        String fileName = fotoUrl.substring(fotoUrl.indexOf("/o/") + 3, fotoUrl.indexOf("?alt=media"));
        deleteFoto(fileName);
    } else {
        throw new NotFoundException("User not found with id: " + id);
    }
}

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


}
