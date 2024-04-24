package com.example.absensireact.impl;

import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Karyawan;
import com.example.absensireact.model.User;
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

import javax.persistence.EntityNotFoundException;
import java.io.FileInputStream;
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

    public KaryawanImpl(UserRepository userRepository, KaryawanRepository karyawanRepository) {
        this.userRepository = userRepository;
        this.karyawanRepository = karyawanRepository;
    }

    @Override
    public List<Karyawan>getAllKaryawan(){
        return karyawanRepository.findAll();
    }

    @Override
    public List<Karyawan> getKaryawanByUserId(Long userId){
        return karyawanRepository.findByUserId(userId);
    }

    @Override
    public Optional<Karyawan> getKaryawanById(Long id){
        return karyawanRepository.findById(id);
    }

    @Override
    public Karyawan TambahKaryawan(Long userId , Karyawan karyawan){
        // Dapatkan objek User dari UserRepository berdasarkan ID
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            karyawan.setUser(user);
            karyawan.setJabatan(karyawan.getJabatan());
            karyawan.setShift(karyawan.getShift());
            karyawan.setUser(user);
            return karyawanRepository.save(karyawan);
        } else {
            throw new EntityNotFoundException("User dengan ID " + karyawan.getUser().getId() + " tidak ditemukan.");
        }
    }
    @Override
    public String uploadFoto(MultipartFile image) throws IOException {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String folderPath = "karyawan/";
        String fileName = folderPath + timestamp + "_" + image.getOriginalFilename();
        BlobId blobId = BlobId.of("absensireact.appspot.com", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType("media")
                .build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./src/main/java/com.example.absensireact/firebase/FirebaseConfig.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, image.getBytes());
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    @Override
    public Karyawan EditKaryawanByUserId(Long userId, Karyawan karyawan, MultipartFile image) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User Id Tidak Ditemukan"));
        karyawan.setUser(user);
        karyawan.setShift(karyawan.getShift());
        karyawan.setJabatan(karyawan.getJabatan());
        String fotoUrl = uploadFoto(image);
        karyawan.setFotoKaryawan(fotoUrl);

        return karyawanRepository.save(karyawan);
    }

    @Override
    public Karyawan EditByid(Long id, Karyawan karyawan, MultipartFile image) throws IOException{
        Optional<Karyawan> karyawan1 = karyawanRepository.findById(id);

        if (karyawan1.isEmpty()) {
            throw new NotFoundException("Karyawan dengan id " + id + " tidak ditemukan");
        }
        karyawan.setJabatan(karyawan.getJabatan());
        karyawan.setShift(karyawan.getShift());
        String fileFoto = uploadFoto(image);
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
