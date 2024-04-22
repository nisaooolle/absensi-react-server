package com.example.absensireact.impl;

import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.exception.ResponseHelper;
import com.example.absensireact.model.Absensi;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.AbsensiRepository;
import com.example.absensireact.repository.UserRepository;
import com.example.absensireact.service.AbsensiService;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.NotActiveException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class AbsensiImpl implements AbsensiService {
    private final AbsensiRepository absensiRepository;

    private final UserRepository userRepository;


    public AbsensiImpl(AbsensiRepository absensiRepository, UserRepository userRepository) {
        this.absensiRepository = absensiRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Absensi> getAllAbsensi(){
        return  absensiRepository.findAll();
    }



    @Override
    public Absensi PostAbsensi(Long userId, MultipartFile image) throws IOException {
        // Mencari User dari repository
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            throw new EntityNotFoundException("User dengan ID " + userId + " tidak ditemukan.");
        }

        // Set waktu masuk
        Date masuk = new Date();

        // Set tanggal hari ini
        Date tanggalHariIni = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(tanggalHariIni);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        tanggalHariIni = cal.getTime();

        // Validasi Keterangan hadir
        Calendar calJamMasuk = Calendar.getInstance();
        calJamMasuk.setTime(masuk);
        int jamMasuk = calJamMasuk.get(Calendar.HOUR_OF_DAY);
        String keterangan = (jamMasuk < 7) ? "Tidak Terlambat" : "Terlambat";

        // Set nilai absensi
        Absensi absensi = new Absensi();
        absensi.setUser(user);
        absensi.setJamMasuk(String.valueOf(masuk));
        absensi.setJamPulang("-");
        absensi.setTanggalAbsen(tanggalHariIni);
        absensi.setKeterangan(keterangan);

        // Upload foto ke Firebase
        String fotoUrl = uploadFoto(image);

        // Set URL foto pada absensi
        absensi.setFotoMasuk(fotoUrl);

        // Simpan absensi ke repository
        return absensiRepository.save(absensi);
    }

    @Override
    public Absensi PutPulang(Long userId, MultipartFile image) throws IOException {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            Absensi absensi = new Absensi();
            // Set waktu pulang
            Date pulang = new Date();
            Calendar calJamMasuk = Calendar.getInstance();
            calJamMasuk.setTime(pulang);
            int jamMasuk = calJamMasuk.get(Calendar.HOUR_OF_DAY);

            if (jamMasuk != 14 ) {

            // Set nilai absensi
            absensi.setJamPulang(String.valueOf(pulang));
            // Upload foto ke Firebase
            String fotoUrl = uploadFoto(image);
            // Set URL foto pada absensi
            absensi.setFotoPulang(fotoUrl);
            return absensiRepository.save(absensi);
            } else {

            throw new NotActiveException("Belum Waktunya Pulang ");
            }

        } else {
            throw new NotFoundException("User not found with id: " + userId);
        }
    }

    @Override
    public String uploadFoto( MultipartFile image) throws IOException {

        // Create a BlobId object with the image data and metadata
        BlobId blobId = BlobId.of("absensireact.appspot.com", image.getOriginalFilename());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType("media")
                .build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./src/main/java/com.example.absensireact/firebase/FirebaseConfig.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, image.getBytes());
        return String.format(DOWNLOAD_URL, URLEncoder.encode(image.getOriginalFilename(), StandardCharsets.UTF_8));
    }
    @Override
    public Optional<Absensi> getAbsensiById(Long id) {
        return absensiRepository.findById(id);
    }

    @Override
    public Absensi updateAbsensi(Long id, Absensi absensi) {
        if (absensiRepository.existsById(id)) {
            absensi.setId(id);
            return absensiRepository.save(absensi);
        } else {
            throw new NotFoundException("Absensi not found with id: " + id);
        }
    }

    @Override
    public void deleteAbsensi(Long id) {
        if (absensiRepository.existsById(id)) {
            absensiRepository.deleteById(id);
        } else {
            throw new NotFoundException("Absensi not found with id: " + id);
        }
    }

    @Override
    public List<Absensi> getAbsensiByUserId(Long userId) {
        return absensiRepository.findByUser_Id(userId);
    }
}
