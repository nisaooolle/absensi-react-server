package com.example.absensireact.impl;

import com.example.absensireact.exception.InternalErrorException;
import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Admin;
import com.example.absensireact.model.Karyawan;
import com.example.absensireact.repository.AdminRepository;
import com.example.absensireact.repository.KaryawanRepository;
import com.example.absensireact.repository.UserRepository;
import com.example.absensireact.service.KaryawanService;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
public class KaryawanImpl implements KaryawanService {

    static final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/absensireact.appspot.com/o/%s?alt=media";

    private final UserRepository userRepository;

      private final KaryawanRepository karyawanRepository;

      private final AdminRepository adminRepository;

    public KaryawanImpl(UserRepository userRepository, KaryawanRepository karyawanRepository, AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.karyawanRepository = karyawanRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public List<Karyawan>getAllKaryawan(){
        return karyawanRepository.findAll();
    }

    @Override
    public Optional<Karyawan> getKaryawanByAdminId(Long adminId){
        return karyawanRepository.findByAdmin(adminId);
    }

    @Override
    public Optional<Karyawan> getKaryawanById(Long id){
        return karyawanRepository.findById(id);
    }

    //    @Override
//    public Karyawan TambahKaryawan(Long userId , Karyawan karyawan){
//        // Dapatkan objek User dari UserRepository berdasarkan ID
//        User user = userRepository.findById(userId).orElse(null);
//
//        if (user != null) {
//            karyawan.setUser(user);
//            karyawan.setJabatan(karyawan.getJabatan());
//            karyawan.setShift(karyawan.getShift());
//            karyawan.setUser(user);
//            return karyawanRepository.save(karyawan);
//        } else {
//            throw new EntityNotFoundException("User dengan ID " + karyawan.getUser().getId() + " tidak ditemukan.");
//        }
//    }
    @Override
    public Karyawan TambahKaryawan(Long adminId, Karyawan karyawan, MultipartFile image) {
        Optional<Admin> userOptional = adminRepository.findByIdAndRole(adminId, "ADMIN");

        if (userOptional.isPresent()) {
            Admin adminUser = userOptional.get();

            Optional<Karyawan> existingKaryawanOptional = karyawanRepository.findKaryawanByadmin(adminUser);
            if (existingKaryawanOptional.isPresent()) {
                throw new EntityExistsException("Pengguna dengan ID " + adminId + " sudah memiliki entri karyawan.");
            }

            karyawan.setAdmin(adminUser);
            karyawan.setFotoKaryawan(imageConverter(image));
            karyawan.setJabatan(karyawan.getJabatan());
            karyawan.setShift(karyawan.getShift());
            return karyawanRepository.save(karyawan);
        } else {
            throw new EntityNotFoundException("Pengguna dengan ID " + adminId + " dan peran ADMIN tidak ditemukan.");
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
        String folderPath = "karyawan/";
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



    @Override
    public Karyawan EditByid(Long id, Karyawan karyawan, MultipartFile image) {
        Optional<Karyawan> karyawan1 = karyawanRepository.findById(id);

        if (karyawan1.isEmpty()) {
            throw new NotFoundException("Karyawan dengan id " + id + " tidak ditemukan");
        }
        karyawan.setJabatan(karyawan.getJabatan());
        karyawan.setShift(karyawan.getShift());
        String fileFoto = imageConverter(image);
        karyawan.setFotoKaryawan(fileFoto);

        return karyawanRepository.save(karyawan);
    }
    @Override
    public void deleteKaryawan(Long id) {
        if (karyawanRepository.existsById(id)) {
            karyawanRepository.deleteById(id);
        } else {
            throw new NotFoundException("Karyawan not found with id: " + id);
        }
    }
}
