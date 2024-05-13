package com.example.absensireact.impl;

import com.example.absensireact.exception.NotFoundException;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class AbsensiImpl implements AbsensiService {
    static final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/absensireact.appspot.com/o/%s?alt=media";

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
    public Absensi PostAbsensi(Long userId, MultipartFile image, String keteranganTerlambat) throws IOException {
        Optional<Absensi> existingAbsensi = absensiRepository.findByUserIdAndTanggalAbsen(userId, truncateTime(new Date()));
        if (existingAbsensi.isPresent()) {
            throw new NotFoundException("User sudah melakukan absensi masuk pada hari yang sama sebelumnya.");
        } else {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User dengan ID " + userId + " tidak ditemukan."));

            Date tanggalHariIni = truncateTime(new Date());
            Date masuk = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            String jamMasukString = formatter.format(masuk);
            String keterangan = (getHourOfDay(masuk) < 7) ? "Tidak Terlambat" : "Terlambat";
            Absensi absensi = new Absensi();
            absensi.setUser(user);
            absensi.setTanggalAbsen(tanggalHariIni);
            absensi.setJamMasuk(jamMasukString);
            absensi.setJamPulang("-");

            absensi.setKeteranganTerlambat(keteranganTerlambat.isEmpty() ? "-" : keteranganTerlambat);
            absensi.setStatusAbsen(keterangan);

            absensi.setFotoMasuk(uploadFile(image, "foto_masuk_" + userId));

            return absensiRepository.save(absensi);
        }
    }


    @Override
    public Absensi Pulang(Long userId, MultipartFile image) throws IOException {
        Optional<Absensi> existingAbsensi = absensiRepository.findByUserIdAndTanggalAbsen(userId, truncateTime(new Date()));
        if (existingAbsensi.isPresent()) {
            Absensi absensi = existingAbsensi.get();
            if (!absensi.getJamPulang().equals("-")) {
                throw new NotFoundException("User sudah melakukan absensi pulang pada hari yang sama sebelumnya.");
            }
            Date masuk = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            String jamPulang = formatter.format(masuk);
            absensi.setJamPulang(jamPulang);
            absensi.setFotoPulang(uploadFilePulang(image, "foto_pulang_" + userId));
            return absensiRepository.save(absensi);
        } else {
            throw new NotFoundException("User belum melakukan absensi masuk pada hari ini.");
        }
    }

    @Override
    public boolean checkUserAlreadyAbsenToday(Long userId) {
        Optional<Absensi> absensi = absensiRepository.findByUserIdAndTanggalAbsen(userId, truncateTime(new Date()));
        return absensi.isPresent();
    }

    private static Date truncateTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }



    private int getHourOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    private String uploadFile(MultipartFile multipartFile, String fileName) throws IOException {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String folderPath = "fotoAbsen/fotoMasuk/";
        String fullPath = folderPath + timestamp + "_" + fileName;
        BlobId blobId = BlobId.of("absensireact.appspot.com", fullPath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./src/main/resources/FirebaseConfig.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, multipartFile.getBytes());
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fullPath, StandardCharsets.UTF_8));
    }

    private String uploadFilePulang(MultipartFile multipartFile, String fileName) throws IOException {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String folderPath = "fotoAbsen/fotoPulang/";
        String fullPath = folderPath + timestamp + "_" + fileName;
        BlobId blobId = BlobId.of("absensireact.appspot.com", fullPath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./src/main/resources/FirebaseConfig.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, multipartFile.getBytes());
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fullPath, StandardCharsets.UTF_8));
    }

    @Override
    public Optional<Absensi> getAbsensiById(Long id) {
        return absensiRepository.findById(id);
    }

    @Override
    public Absensi updateAbsensi(Long id, Absensi absensi) {
        return absensiRepository.findById(id)
                .map(existingAbsensi -> {
                    existingAbsensi.setTanggalAbsen(absensi.getTanggalAbsen());
                    existingAbsensi.setJamMasuk(absensi.getJamMasuk());
                    existingAbsensi.setJamPulang(absensi.getJamPulang());
                    existingAbsensi.setLokasiMasuk(absensi.getLokasiMasuk());
                    existingAbsensi.setLokasiPulang(absensi.getLokasiPulang());
                    existingAbsensi.setKeteranganTerlambat(absensi.getKeteranganTerlambat());
                    existingAbsensi.setFotoMasuk(absensi.getFotoMasuk());
                    existingAbsensi.setFotoPulang(absensi.getFotoPulang());
                    existingAbsensi.setStatus(absensi.getStatus());
                    existingAbsensi.setStatusAbsen(absensi.getStatusAbsen());
                    existingAbsensi.setKeteranganIzin(absensi.getKeteranganIzin());
                    existingAbsensi.setKeteranganPulang(absensi.getKeteranganPulang());
                    existingAbsensi.setKeteranganPulangAwal(absensi.getKeteranganPulangAwal());
                    return absensiRepository.save(existingAbsensi);
                })
                .orElseThrow(() -> new NotFoundException("Absensi not found with id: " + id));
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
        return absensiRepository.findabsensiByUserId(userId);
    }
}
